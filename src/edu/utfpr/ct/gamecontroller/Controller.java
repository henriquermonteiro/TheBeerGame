package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.gamecontroller.config.DefaultConfiguration;
import edu.utfpr.ct.logmanager.Logger;
import java.util.HashMap;
import java.util.Map;

public class Controller
{
	private final Map<Integer, Game> games;
	private final Logger logger;

	public Controller()
	{
		this.games = new HashMap<>();
		this.logger = new Logger();
	}

	public Game newGame()
	{
		Game newGame;

		newGame = new DefaultConfiguration().configure();
		games.put(newGame.gameID, newGame);

		return newGame;
	}

	public Game resumeGame(int gameID)
	{
		return games.get(gameID);
	}

	public Game[] RetrieveUnfinishedGames()
	{
		logger.RetrieveUnfinishedGames();

		return null;
	}

	public void finishGame(int gameID)
	{
		generateReport(gameID);
		logger.deleteGame(gameID);
		games.remove(gameID);
	}

	public void placeDemand(int gameID, Function function, int demand)
	{
		Node node;

		node = (Node) games.get(gameID).supplyChain.get(function.getPosition());
		node.playerMove.add(demand);
		logger.saveGame(games.get(gameID));
	}

	public void lineWalk(int gameID, Function function, int demand)
	{
//		Game game = games.get(gameID);
//		AbstractNode previous = null;
//		int demand2 = game.demand
//
//		for(AbstractNode an : game.supplyChain)
//		{
//			if(previous != null)
//				previous.travellingStock += an.travellingStock;
//
//			an.travellingStock = 0;
//			previous = an;
//		}

		/*for(ActivePoint ap : playersNode)
		{
			ap.currentStock += ap.travellingStock;
		}*/
	}

	private void generateReport(int gameID)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
