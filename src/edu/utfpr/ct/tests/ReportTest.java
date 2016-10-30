package edu.utfpr.ct.tests;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.gamecontroller.CSVReport;
import edu.utfpr.ct.gamecontroller.HTMLReport;
import edu.utfpr.ct.interfaces.IReport;

public class ReportTest
{
	public void test()
	{
		IReport report;

		Game game = new GameBuilderTest().test();

		report = new CSVReport();
		report.generateReport(game);

		report = new HTMLReport();
		report.generateReport(game);
	}
}
