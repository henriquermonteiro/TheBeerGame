package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IReport
{
    public boolean generateReport(Game game);
	
	public Game[] loadReports();
	
	public boolean purgeReport(Game game);
}
