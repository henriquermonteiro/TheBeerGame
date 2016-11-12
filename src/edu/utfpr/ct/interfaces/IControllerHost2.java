package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IControllerHost2
{
	boolean createGame(Game game) throws IllegalArgumentException;

	Game[] getGames();

	Game[] getReports();

	String[] getPlayersOnGame(String gameName);

	Game getGame(String gameName);
	
	int getGameState(String gameName);

	Game getReport(String gameName);

	boolean purgeGame(String gameName);

	boolean purgeReport(String gameName);

	boolean startGame(String gameName);

	boolean pauseGame(String gameName);

	boolean addPlayerOnGame(String gameName, String playerName);

	boolean purgePlayerOnGame(String gameName, String playerName);

	boolean changePlayerForNode(String gameName, IFunction function, String playerName);

	boolean removePlayerFromNode(String gameName, IFunction function);

	int postMoveForNode(String gameName, int order) throws IllegalStateException, IllegalArgumentException;
}
