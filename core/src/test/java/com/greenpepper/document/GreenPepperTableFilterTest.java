package com.greenpepper.document;

import junit.framework.TestCase;

import com.greenpepper.Example;
import com.greenpepper.util.ExampleUtil;
import com.greenpepper.util.Tables;

public class GreenPepperTableFilterTest extends TestCase
{
     public void testShouldSkipSpecificationInLazyModeIfNoGreenPepperTestTags()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(true);
         
         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n"
         );
         assertTrue( filter.canFilter(example) );
         assertNull( filter.filter(example) );
     }

     
     public void testShouldSkipSpecificationInLazyModeIfNoGreenPepperEndTestTag()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(true);

         Example example = Tables.parse(
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table]\n" +
             "[with rows]\n"
         );
         assertTrue(filter.canFilter(example));
         assertNull(filter.filter(example));
     }

     
     public void testShouldNotSkipSpecificationInEagerModeIfNoGreenPepperTestTags()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(false);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n"
         );
         assertFalse(filter.canFilter(example));
         assertEquals("table", ExampleUtil.contentOf(filter.filter(example).at(0, 0, 0)));
     }

     
     public void testShouldNotSkipSpecificationInLazyModeIfNoGreenPepperEndTestTag()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(false);

         Example example = Tables.parse(
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table]\n" +
             "[with rows]\n"
         );
         assertTrue(filter.canFilter(example));
         assertEquals("table", ExampleUtil.contentOf(filter.filter(example).at(0, 0, 0)));
     }

     
     public void testShouldSkipSpecificationInLazyModeToEndGreenPepperTag()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(true);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table1]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n"
         );
         assertTrue(filter.canFilter(example));
         Example filtered = filter.filter(example);
         assertNotNull(filtered);
         assertEquals( "table1", ExampleUtil.contentOf(filtered.at(0,0,0)) );
     }

     
     public void testShouldSkipGreenPepperTagInNotLazyMode()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(false);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table1]\n"
         );
         assertFalse(filter.canFilter(example));
         Example filtered = example;
         filtered = filter.filter(filtered);
         assertEquals("table", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());
         assertNotNull(filtered);
         assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));
     }

     
     public void testShouldGetOnlyElementsWithingGreenPepperTags()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(true);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table1]\n" + 
             "****\n" + 
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" +
             "****\n" +
             "[table2]\n" + 
             "****\n"
         );
         assertTrue(filter.canFilter(example));
         Example filtered = filter.filter(example);

         assertNotNull(filtered);
         assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());

         assertNull(filtered);
     }

     
     public void testShouldSkipBeginAndEndGreenPepperTags()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(false);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table1]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" +
             "****\n" +
             "[table2]\n" +
             "****\n"
         );
         Example filtered = filter.filter(example);

         assertNotNull(filtered);
         assertEquals("table", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         filtered = filter.filter(filtered.nextSibling());

         assertNotNull(filtered);
         assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         filtered = filter.filter(filtered.nextSibling());

         assertNotNull(filtered);
         assertEquals("table2", ExampleUtil.contentOf(filtered.at(0, 0, 0)));
     }

     
     public void testShouldGetAllElementsAfterBeginGreenPepperTagToTheEndGreenPepperTag()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(true);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table1]\n" +
             "****\n" +
             "[table2]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n"
         );
         assertTrue(filter.canFilter(example));
         Example filtered = filter.filter(example);

         assertNotNull(filtered);
         assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));
         
         assertFalse(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());
         assertEquals("table2", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());

         assertNull(filtered);
     }

     
     public void testWeCanCombineGreenPepperTags()
     {
         GreenPepperTableFilter filter = new GreenPepperTableFilter(true);

         Example example = Tables.parse(
             "[table]\n" +
             "[with rows]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table1]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table2]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.BEGIN_GP_TEST + "]\n" +
             "****\n" +
             "[table2bis]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" +
             "****\n" +
             "[table3]\n" +
             "****\n" +
             "[" + GreenPepperTableFilter.END_GP_TEST + "]\n" + 
             "****\n" +
             "[table4]\n"
         );

         assertTrue(filter.canFilter(example));
         Example filtered = filter.filter(example);

         assertNotNull(filtered);
         assertEquals("table1", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());
         assertEquals("table2", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());
         assertEquals("table2bis", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());

         assertNotNull(filtered);
         assertEquals("table3", ExampleUtil.contentOf(filtered.at(0, 0, 0)));

         assertTrue(filter.canFilter(filtered.nextSibling()));
         filtered = filter.filter(filtered.nextSibling());

         assertNull(filtered);
     }
}
