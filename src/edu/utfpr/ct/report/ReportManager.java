package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IReport;

public class ReportManager implements IReport
{
	@Override
	public boolean createReport(Game game)
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
	public boolean purgeReport(Game game)
	{
		AbstractReport report;
		boolean status;

		report = new CSVReport();
		status = report.deleteFile(report.getFileName(game));

		report = new HTMLReport();
		status &= report.deleteFile(report.getFileName(game));

		report = new BinaryReport();
		status &= report.deleteFile(report.getFileName(game));

		return status;
	}

	@Override
	public Game[] getReports()
	{
		AbstractReport report;
		Game[] game;

		report = new BinaryReport();
		game = report.loadReport();

		return game;
	}
}
