package com.greenpepper.extensions.fit;

import com.greenpepper.AlternateCalculator;
import com.greenpepper.Example;
import com.greenpepper.document.Document;
import com.greenpepper.interpreter.RuleForInterpreter;
import com.greenpepper.systemunderdevelopment.DefaultSystemUnderDevelopment;
import com.greenpepper.util.Tables;
import com.greenpepper.util.URIUtil;
import eg.music.Browser;
import junit.framework.TestCase;


public class FitInterpreterSelectorTest extends TestCase
{
	private FitInterpreterSelector interpreterSelector = new FitInterpreterSelector(new DefaultSystemUnderDevelopment());
	
	public void testWeCanExecuteAColumnFixtureExample()
	{
		Document doc = Document.text(columnFitTable());
		doc.execute(interpreterSelector);

		assertEquals(0, doc.getStatistics().wrongCount());
		assertEquals(14, doc.getStatistics().rightCount());
		assertEquals(6, doc.getStatistics().ignoredCount());
		assertEquals(0, doc.getStatistics().exceptionCount());
		
		doc = Document.text(anotherColumnFitTable());
		doc.execute(interpreterSelector);

		assertEquals(1, doc.getStatistics().wrongCount());
		assertEquals(7, doc.getStatistics().rightCount());
		assertEquals(0, doc.getStatistics().ignoredCount());
		assertEquals(0, doc.getStatistics().exceptionCount());
	}
	
	public void testTheFallbackIsCalledIfCantSelectAFitInterpreter()
	{
		Document doc = Document.text(ruleForTable());
		doc.execute(interpreterSelector);

		assertEquals(1, doc.getStatistics().wrongCount());
		assertEquals(1, doc.getStatistics().rightCount());
		assertEquals(0, doc.getStatistics().ignoredCount());
		assertEquals(0, doc.getStatistics().exceptionCount());
	}
	
	public void testWeCanExecuteARowFixtureExample() throws Exception
	{
		Browser browser = new Browser();
		browser.library(URIUtil.decoded(FitInterpreterSelectorTest.class.getResource("Music.txt").getPath()));
		browser.showAll();
		
		Document doc = Document.text(rowFitTable());
		doc.execute(interpreterSelector);

		assertEquals(32, doc.getStatistics().wrongCount());
		assertEquals(5, doc.getStatistics().rightCount());
		assertEquals(0, doc.getStatistics().ignoredCount());
		assertEquals(0, doc.getStatistics().exceptionCount());
	}
	
	public void testWeCanExecuteAnActionFixtureExample() throws Exception
	{
		Browser browser = new Browser();
		browser.library(URIUtil.decoded(FitInterpreterSelectorTest.class.getResource("Music.txt").getPath()));
		browser.showAll();
		
		Document doc = Document.text(actionFitTable());
		doc.execute(interpreterSelector);

		assertEquals(0, doc.getStatistics().wrongCount());
		assertEquals(8, doc.getStatistics().rightCount());
		assertEquals(0, doc.getStatistics().ignoredCount());
		assertEquals(0, doc.getStatistics().exceptionCount());
	}
	
	private Example columnFitTable()
	{
		return Tables.parse(
				"[eg.Calculator]\n" +
				"[key]	[x()][y()][z()][t()]\n" +
				"[100]	[100.0][] []   []	\n" +
				"[enter][100][100][]   []	\n" + 	 
				"[enter][100][100][100][]	\n" + 
				"[enter][100][100][100][100]\n" +
				"[clx]	[0]	 [100][100][100]\n" +
				"****\n"
				);
	}
	
	private Example anotherColumnFitTable()
	{
		return Tables.parse(
			"[eg.Calculator]\n" +
			"[volts][watts()][points()]\n" +
			"[3.75]	[.500]	[false]\n" +
			"[3.60]	[.500]	[false]\n" +
			"[3.45]	[.500]	[false]\n" +
			"[3.30]	[.500]	[true]\n" +
			"****\n"
		);
	}
	
	private Example rowFitTable()
	{
		return Tables.parse(
			"[eg.music.Display]\n" +
			"[title] 				   [album] 			[genre] [size]		[date]\n" +
			"[Another Grey Morning]    [JT] 			[Pop] 	[3284199] 	[9/7/02 11:32 PM]\n" +
			"[Ananas] 				   [Hourglass] 		[Pop] 	[6897450] 	[9/7/02 11:47 PM]\n" +
			"[Copperline] 			   [New Moon Shine] [Pop] 	[5248087] 	[9/7/02 9:52 PM]\n" +
			"[Handy Man] 			   [JT] 			[Pop] 	[3976956] 	[9/7/02 11:36 PM]\n" +
			"[Sailing To Philadelphia] [October Rose] 	[Pop] 	[6581911] 	[9/7/02 10:45 PM]\n" +
			"****\n"
		);
	}
	
	private Example actionFitTable()
	{ 
		return Tables.parse(
			"[fit.ActionFixture]\n" +
			"[start] 	[eg.music.Browser]\n" +
			"[enter] 	[select] 	[1] [] [Comment]\n" +
			"[check]	[title]		[Akila]\n" +
			"[check] 	[artist] 	[Toure Kunda]\n" +
			"[enter] 	[select] 	[2]\n" +
			"[check] 	[title] 	[American Tango]\n" +
			"[check] 	[artist] 	[Weather Report]\n" +
			"[check] 	[album] 	[Mysterious Traveller]\n" +
			"[check] 	[year] 		[1974]\n" +
			"[check] 	[time] 		[3.70]\n" +
			"[check]	[track] 	[2 of 7]\n" +
			"****\n"
		);
	}
    
    public Example doFitTable()
    {
    	return Tables.parse(
    		"[fitlibrary.eg.chat.ChatStart]\n" +
            "****\n" +
    		"[connect user] [anna]\n" +
    		"[creates] [lotr]	[room]\n" +
    		"[enters]  [lotr]	[room]\n" +
            "****\n" +
    		"[room]    [lotr]\n" +
    		"[check]   [owner]	[anna]\n" +
    		"[rename]  [LOTR]	\n" +
            "****\n" +
    		"[check]   [occupant count]	[LOTR] [1]\n" +
            "****\n"
        );
    }

    public Example ruleForTable()
    {
    	return Tables.parse(
            "[" + RuleForInterpreter.class.getName() + "][" + AlternateCalculator.class.getName() + "]\n" +
            "[a][b][sum?]\n" +
            "[6][2][8]\n" +
            "[5][2][8]\n" +
            "****\n"
        );
    }
}
