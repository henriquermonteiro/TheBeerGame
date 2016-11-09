package edu.utfpr.ct.interfaces;

import edu.utfpr.ct.datamodel.Game;

public interface IControllerHost2
{
	public boolean createGame(Game game) throws IllegalArgumentException;

	public Game[] getGames();

	public Game[] getReports();

	public String[] getPlayersOnGame(String gameName);

	public Game getGame(String gameName);
	
	public int getGameState(String gameName);

	public Game getReport(String gameName);

	public boolean purgeGame(String gameName);

	public boolean purgeReport(String gameName);

	public boolean startGame(String gameName);

	public boolean pauseGame(String gameName);

	public boolean addPlayerOnGame(String gameName, String playerName);

	public boolean purgePlayerOnGame(String gameName, String playerName);

	public boolean changePlayerForNode(String gameName, IFunction function, String playerName);

	public boolean removePlayerFromNode(String gameName, IFunction function);

	public int postMoveForNode(String gameName, int order) throws IllegalStateException, IllegalArgumentException;
}
