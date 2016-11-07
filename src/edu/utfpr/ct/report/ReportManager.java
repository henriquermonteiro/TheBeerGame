package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IReport;

public class ReportManager implements IReport
{
	@Override
	public boolean generateReport(Game game)
	{
		AbstractReport report;

		report = new CSVReport();
		report.generateReport(game);

		report = new HTMLReport();
		report.generateReport(game);

		report = new BinaryReport();
		report.generateReport(game);

		return true;
	}

	@Override
	//public Game[] getGameReport(String gameName)
	//public Game[] getAvailableReports()
	public Game[] loadReports()
	{
		AbstractReport report;
		Game[] game;

		report = new BinaryReport();
		game = report.loadReport();

		return game;
	}

	@Override
	public boolean purgeReport(Game game)
	{
		AbstractReport report;

		report = new BinaryReport();
		return report.deleteFile(report.getFileName(game));
	}
}
