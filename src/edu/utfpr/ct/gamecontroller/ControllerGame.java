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
	 * Makes all the order placed go from one Node to the other.
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

		if(function == null)
			makeOrderRecursion(gameID, game.supplyChain, 0, order);
		else
		{
			posCurrentNode = (function.getPosition() - 1) * game.deliveryDelay + (function.getPosition() - 1);
			node = (Node) games.get(gameID).supplyChain[posCurrentNode];

			logger.logPlayerMove(order, node);
			node.currentStock += makeOrderRecursion(gameID, game.supplyChain, posCurrentNode + 1, order);
		}
	}

	private int makeOrderRecursion(int gameID, AbstractNode[] an, int posCurrentNode, int order)
	{
		int travellingStock;
		Node node;

		if(posCurrentNode == an.length)
			return order;

		if(an[posCurrentNode] instanceof TravellingTime)
		{
			travellingStock = an[posCurrentNode].travellingStock;
			an[posCurrentNode].travellingStock = makeOrderRecursion(gameID, an, posCurrentNode + 1, order);

			return travellingStock;
		}

		if(an[posCurrentNode] instanceof Node)
		{
			node = (Node) an[posCurrentNode];
			calculateProfit(gameID, node, order);
			node.travellingStock = node.currentStock >= order ? order : node.currentStock;
			node.currentStock = node.currentStock >= order ? node.currentStock - order : 0;

			return node.travellingStock;
		}

		return -1;
	}

	public void calculateProfit(int gameID, Node node, int order)
	{
		Game game = games.get(gameID);

		/* Profit from selling order */
		node.profit += node.currentStock >= order ? order * game.sellingUnitProfit : node.currentStock * game.sellingUnitProfit;
		/* Cost from missing unit */
		node.profit -= node.currentStock >= order ? 0 : (order - node.currentStock) * game.missingUnitCost;
		/* Cost from the remaining stock after selling */
		node.profit -= node.currentStock >= order ? (node.currentStock - order) * game.stockUnitCost : 0;
	}
}
