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
	
	/**
	 * Makes all the orders placed go from one Node to the other.
	 * 
	 * @param gameID
	 * @param function
	 * @param order
	 */
	public void makeOrder(int gameID, Function function, int order)
	{
		int posCurrentNode, posNextNode;
		Game game;
		Node node;
		TravellingTime travellingTime;
		AbstractNode previous = null;

		game = games.get(gameID);
		posCurrentNode = (function.getPosition() - 1) * game.deliveryDelay + (function.getPosition() - 1);
		posNextNode = posCurrentNode + game.deliveryDelay + 1;

		logger.logPlayerMove(order, (Node) game.supplyChain[posCurrentNode]);
		node = (Node) game.supplyChain[posCurrentNode];

		/* A ideia aqui é só passar todos as cervejas para frente, apenas as que estão em viagem, não o último nó pq ele pode não existir */
		for(int i = posCurrentNode; i < (posCurrentNode + game.deliveryDelay); i++)
		{
			if(previous == null)
				previous = game.supplyChain[i];

			previous.travellingStock = game.supplyChain[i].travellingStock;
		}

		/* Manda de currentStock o pedido que chegou */
		node.currentStock += node.travellingStock;
		node.travellingStock = 0;

		/* É o último elo da cadeia, tem tratamento especial, a ordem é colocada no último caminhão invés de no próximo não-existente nó */
		if(function.getPosition() == Function.values().length)
		{
			travellingTime = (TravellingTime) game.supplyChain[posNextNode - 1];
			travellingTime.travellingStock = order;
		}
		else
		{
			node = (Node) game.supplyChain[posNextNode];
			node.travellingStock = node.currentStock >= order ? node.currentStock - order : order;
			node.currentStock = node.currentStock >= order ? node.currentStock - order : 0;
		}
	}
}
