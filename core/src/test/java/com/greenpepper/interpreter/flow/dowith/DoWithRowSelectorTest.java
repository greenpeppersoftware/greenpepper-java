package com.greenpepper.interpreter.flow.dowith;

import static org.hamcrest.Matchers.is;

import com.greenpepper.Assertions;
import com.greenpepper.Example;
import com.greenpepper.Specification;
import com.greenpepper.document.FakeSpecification;
import com.greenpepper.interpreter.flow.Row;
import com.greenpepper.reflect.PlainOldFixture;
import com.greenpepper.util.Rows;
import com.greenpepper.util.TestCase;

public class DoWithRowSelectorTest extends TestCase
{
    private DoWithRowSelector selector;

    protected void setUp() throws Exception
    {
        selector = new DoWithRowSelector(new PlainOldFixture(new Object()));
    }

    public void testInstantiatesRowIfFirstCellOfExampleContainsFullyQualifiedClassName()
    {
        Rows rows = Rows.parse(
                "[" + AcceptRow.class.getName() + "]"
        );
        Row row = selector.select(rows);
        assertThat(row, is(AcceptRow.class));
    }

    public void testThatRowInterfaceIsNotConsideredARow()
    {
        selector.addSuffix("Row");
        selector.searchPackage(packageName(Row.class));
        Rows rows = Rows.parse("[]");  // inferred row class is Row interface
        Row row = selector.select(rows);
        assertThat(row, is(DefaultRow.class));
    }

    public void testSearchesForRowClassInIncludedPackages()
    {
        Rows rows = Rows.parse(
                "[RejectRow]"
        );
        selector.searchPackage(packageName(RejectRow.class));
        Row row = selector.select(rows);
        assertThat(row, is(RejectRow.class));
    }

    private String packageName(Class aClass)
    {
        return aClass.getPackage().getName();
    }

    public void testThatIncludedSuffixesAreOptionalInRowClassNames()
    {
        Rows rows = Rows.parse(
                "[ " + packageName(CheckRow.class) + ".Check]"
        );
        selector.addSuffix("Row");
        Row row = selector.select(rows);
        assertThat(row, is(CheckRow.class));
    }

    public void testThatDefaultRowPackageIsIncludedByDefault() {
        Rows rows = Rows.parse(
            "[RejectRow]"
        );
        Row row = selector.select( rows );
        assertThat(row, is( RejectRow.class ));
    }

    public void testThatRowSuffixIsAddedByDefault() {
        Rows rows = Rows.parse(
            "[Check]"
        );
        Row row = selector.select( rows );
        assertThat(row, is( CheckRow.class ));
    }

    public void testSupportsHumanizedClassNames()
    {
        Rows rows = Rows.parse(
                "[accept row]"
        );
        selector.searchPackage(packageName(AcceptRow.class));
        Row row = selector.select(rows);
        assertThat(row, is(AcceptRow.class));
    }

    public void testRecognizesInterpreterNames()
    {
        Rows rows = Rows.parse(
                "[rulE For]"
        );
        Row row = selector.select(rows);
        assertThat(row, is(InterpretRow.class));
    }

    public void testDefaultsToSelectingDefaultRow()
    {
        Rows rows = Rows.parse(
                "[anything else]"
        );
        Row row = selector.select(rows);
        assertThat(row, is(DefaultRow.class));
    }

    public void testShouldSkipWhenRowClassCannotBeInstantiated()
    {
        Rows rows = Rows.parse(
                "[" + UninstantiableRow.class.getName() + "]"
        );
        Row row = selector.select(rows);
        assertThat(row, is(SkipRow.class));
        FakeSpecification spec = specification(rows);
        row.interpret(spec);
        Assertions.assertAnnotatedException(rows.firstChild());
        assertFalse(spec.hasMoreExamples());
    }

    private FakeSpecification specification(Example example)
    {
        return new FakeSpecification(example);
    }

    public static class UninstantiableRow implements Row
    {

        private UninstantiableRow()
        {
        }

        public void interpret(Specification row)
        {
        }
    }

}
