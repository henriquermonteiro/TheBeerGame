package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.interfaces.ILogger;
import edu.utfpr.ct.logmanager.Logger;
import java.util.HashMap;
import java.util.Map;

public class ControllerGame
{
	private final Map<Integer, Game> games;
	private final ILogger logger;

	public ControllerGame()
	{
		this.games = new HashMap<>();
		this.logger = new Logger();
	}

	public void putGame(Game game)
	{
		games.put(game.gameID, game);
	}

	/**
	 * Makes all the orders placed go from one Node to the other.
	 *
	 * @param gameID
	 * @param function
	 * @param order
	 */
	public void makeOrder(int gameID, Function function, int order)
	{
		int posCurrentNode;
		Game game;
		Node node;
		
		game = games.get(gameID);
		posCurrentNode = (function.getPosition() - 1) * game.deliveryDelay + (function.getPosition() - 1);
		node = (Node) games.get(gameID).supplyChain[posCurrentNode];
		
		logger.logPlayerMove(order, node);
		node.currentStock += makeOrderRecursion(game.supplyChain, posCurrentNode + 1, order);
	}
	
	private int makeOrderRecursion(AbstractNode[] an, int posCurrentNode, int order)
	{
		int travellingStock;
		Node node;
		
		if(posCurrentNode == an.length)
			return order;
		
		if(an[posCurrentNode] instanceof TravellingTime)
		{
			travellingStock = an[posCurrentNode].travellingStock;
			an[posCurrentNode].travellingStock = makeOrderRecursion(an, posCurrentNode + 1, order);
			
			return travellingStock;
		}
		
		if(an[posCurrentNode] instanceof Node)
		{
			node = (Node) an[posCurrentNode];
			travellingStock = node.travellingStock;
			node.travellingStock = node.currentStock >= order ? order : node.currentStock;
			node.currentStock = node.currentStock >= order ? node.currentStock - order : 0;
			
			return travellingStock;
		}
		
		return -1;
	}
}
