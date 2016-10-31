package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game2;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.util.ArrayList;

public class MountGame
{
	public Game2 mountGame(int gameID,
						   String name,
						   String password,
						   double missingUnitCost,
						   double stockUnitCost,
						   double sellingUnitProfit,
						   int realDuration,
						   int informedDuration,
						   int deliveryDelay,
						   int unitiesOnTravel,
						   int initialStock,
						   boolean informedChainSupply)
	{
		Game2 game;
		int lineSize;

		game = new Game2();
		game.gameID = gameID;
		game.name = name;
		game.password = password;

		game.missingUnitCost = missingUnitCost;
		game.stockUnitCost = stockUnitCost;
		game.sellingUnitProfit = sellingUnitProfit;

		game.realDuration = realDuration;
		game.informedDuration = informedDuration;

		game.deliveryDelay = deliveryDelay;
		game.unitiesOnTravel = unitiesOnTravel;

		game.initialStock = initialStock;
		game.demand = new int[realDuration];

		lineSize = Function.values().length;
		lineSize = lineSize + lineSize * deliveryDelay;
		game.supplyChain = new AbstractNode[lineSize];
		game.informedChainSupply = informedChainSupply;

		mountSupplyChain(game);
		
		return game;
	}

	public void mountSupplyChain(Game2 game)
	{
		int position;
		Node node;
		TravellingTime travellingTime;

		position = 0;
		for(Function value : Function.values())
		{
			node = new Node();
			node.travellingStock = 0;
			node.currentStock = game.initialStock;
			node.function = value;
			node.profit = 0;
			node.playerMove = new ArrayList<>();
			game.supplyChain[position] = node;
			position++;

			for(; position < game.deliveryDelay; position++)
			{
				travellingTime = new TravellingTime();
				travellingTime.travellingStock = game.unitiesOnTravel;
				game.supplyChain[position] = travellingTime;
			}
		}
	}

	public void mountDemand(Game2 game)
	{
	}

	public void mountPlayerMoves(Node node, int[] moves)
	{
		for(int i = 0; i < moves.length; i++)
			node.playerMove.add(moves[i]);
	}
}
