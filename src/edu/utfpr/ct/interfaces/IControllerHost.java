package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IControllerHost
{
	boolean createGame(Game game) throws IllegalArgumentException;

	Game[] getGames();

	Game[] getReports();

	String[] getPlayersOnGame(String gameName);

	Game getGame(String gameName);
	
	int getGameWeek(String gameName);
	
	int getGameState(String gameName);
	
	int getReportState(String gameName);

	Game getReport(String gameName);

	boolean purgeGame(String gameName);

	boolean purgeReport(String gameName);

	boolean startGame(String gameName);

	boolean pauseGame(String gameName);

	boolean startReport(String gameName);

	boolean pauseReport(String gameName);

	boolean addPlayerOnGame(String gameName, String playerName);

	boolean purgePlayerOnGame(String gameName, String playerName);

	boolean changePlayerForNode(String gameName, IFunction function, String playerName);

	boolean removePlayerFromNode(String gameName, IFunction function);

	int postMoveForNode(String gameName, int order) throws IllegalStateException, IllegalArgumentException;
}
