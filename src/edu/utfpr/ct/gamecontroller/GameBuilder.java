package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class GameBuilder
{
	public Game buildGame(Game game)
	{
		Calendar calendar = new GregorianCalendar();

		game.gameID = 0;
		game.timestamp = calendar.getTimeInMillis();
		buildDemand(game);
		buildSupplyChain(game);

		return game;
	}

	public void buildDemand(Game game)
	{
		game.demand = new int[game.realDuration];

		for(int i = 0; i < game.demand.length; i++)
			game.demand[i] = 0;
	}

	public void buildSupplyChain(Game game)
	{
		int chainSize, position;
		Node node;
		TravellingTime travellingTime;

		chainSize = Function.values().length;
		chainSize = chainSize + chainSize * game.deliveryDelay;
		game.supplyChain = new AbstractNode[chainSize];

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

			for(int i = 0; i < game.deliveryDelay; i++, position++)
			{
				travellingTime = new TravellingTime();
				travellingTime.travellingStock = game.unitiesOnTravel;
				game.supplyChain[position] = travellingTime;
			}
		}
	}

	public void buildPlayerMoves(Node node, int[] moves)
	{
		node.playerMove = new ArrayList<>();

		for(int i = 0; i < moves.length; i++)
			node.playerMove.add(moves[i]);
	}
}
