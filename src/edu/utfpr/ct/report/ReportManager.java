package edu.utfpr.ct.report;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.gamecontroller.Table;
import edu.utfpr.ct.interfaces.IReport;

public class ReportManager implements IReport
{
	private static ReportManager reportManager;

	private ReportManager()
	{
	}

	public static synchronized ReportManager getReportManager()
	{
		if(reportManager == null)
			reportManager = new ReportManager();

		return reportManager;
	}

	@Override
	public boolean createReport(Table table)
	{
		AbstractReport report;

		report = new CSVReport();
		report.generateReport(table);

		report = new HTMLReport();
		report.generateReport(table);

		report = new BinaryReport();
		report.generateReport(table);

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
		Game[] games;

		report = new BinaryReport();
		games = report.loadReports();

		return games;
	}
}
