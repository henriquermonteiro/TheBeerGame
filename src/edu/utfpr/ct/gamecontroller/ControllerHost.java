package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.interfaces.ILogger;
import edu.utfpr.ct.logmanager.Logger;
import java.util.HashMap;
import java.util.Map;

public class ControllerHost
{
	private final Map<Integer, Game> games;
	private final ILogger logger;

	public ControllerHost()
	{
		this.games = new HashMap<>();
		this.logger = new Logger();
	}
	
	public boolean createGame(Game game)
	{
		GameBuilder gameBuilder = new GameBuilder();
		
		gameBuilder.buildGame(game);
		games.put(game.gameID, game);
		logger.logGameStart(game);
		
		return true;
	}

	public boolean restoreGame(Integer gameID)
	{
		Game game;
		
		game = logger.retrieveGameData(gameID);
		games.put(gameID, game);
		
		return true;
	}

	public boolean startGame(Integer gameID)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean stopGame(Integer gameID)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean purgeGame(Integer gameID)
	{
		logger.purgeGame(gameID);
		games.remove(gameID);
		
		return true;
	}

	public Game getGameReport(String gameName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public String[] getAvailableReports()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean purgeReport(String gameName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean changePlayerForNode(Integer gameID, Integer nodeID, String playerName)
	{
		Node node;
		
		node = (Node) games.get(gameID).supplyChain[nodeID];
		node.playerName = playerName;
		/* Tem que atualizar no log tbm */
		
		return true;
	}

	public boolean removePlayerFromNode(Integer gameID, Integer nodeID)
	{
		Node node;
		
		node = (Node) games.get(gameID).supplyChain[nodeID];
		node.playerName = "";
		/* Tem que atualizar no log tbm */
		
		return true;
	}

	public Integer postMoveForNode(Integer gameID, Integer nodeID, Integer move)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public Game getGameRoomData(Integer gameID)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
}
