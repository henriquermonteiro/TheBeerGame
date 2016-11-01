package edu.utfpr.ct.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.GameBuilder;
import java.util.ArrayList;

public class GameBuilderTest
{
	public Game test()
	{
		return configureGame();
	}

	private Game configureGame()
	{
		Game game;
		GameBuilder gb;

		game = new Game();
		game.name = "Test";
		game.password = "Test";
		game.missingUnitCost = 1.0;
		game.stockUnitCost = 2.0;
		game.sellingUnitProfit = 3.0;
		game.realDuration = 36;
		game.informedDuration = 50;
		game.deliveryDelay = 2;
		game.unitiesOnTravel = 16;//4;
		game.initialStock = 16;
		game.informedChainSupply = true;

		gb = new GameBuilder();
		gb.buildGame(game);
		
		for(int i = 0; i < game.demand.length; i++)
		{
			if(i < 4)
				game.demand[i] = 4;
			else
				game.demand[i] = 8;
		}

		gb.buildSupplyChain(game);
		configureSupplyChain(game);
		
		return game;
	}

	private void configureSupplyChain(Game game)
	{
		int position;
		Node node;

		position = 0;
		for(AbstractNode abstractNode : game.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;

			node = (Node) abstractNode;
			node.playerName = "Test " + position;
			node.profit = 0;//Math.random() * 100;
			node.playerMove = new ArrayList<>();
			position++;

			for(int i = 0; i < game.realDuration; i++)
			{
				//node.playerMove.add(Math.abs(random.nextInt()));
				node.playerMove.add(position * game.demand[i]);
			}
		}
	}
}
