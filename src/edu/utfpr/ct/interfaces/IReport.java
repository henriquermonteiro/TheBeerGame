package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IReport
{
	public boolean createReport(Game game);

	public boolean purgeReport(Game game);

	public Game[] getReports();
}
