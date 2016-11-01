package edu.utfpr.ct.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.ControllerGame;

public class PlaceOrderTest
{
	public void test()
	{
		GameBuilderTest gbt;
		Game game;

		gbt = new GameBuilderTest();
		game = gbt.test();

		//placeOrder(game);
		placeOrder2(game);
	}

	private void placeOrder(Game game)
	{
		ControllerGame cg = new ControllerGame();
		cg.putGame(game);

		for(int i = 0; i < game.realDuration; i++)
		{
			//((Node) game.supplyChain[0]).currentStock -= game.demand[i];
			cg.makeOrder(game.gameID, null, game.demand[i]);
			printSupplyChain(game.supplyChain);
			printSupplyChainProfit(game.supplyChain);
			
			for(Function value : Function.values())
			{
				cg.makeOrder(game.gameID, value, game.demand[i]);
				System.out.print(i + " -" + game.demand[i] + ": ");
				printSupplyChain(game.supplyChain);
				printSupplyChainProfit(game.supplyChain);
			}
		}
	}
	
	private void placeOrder2(Game game)
	{
		ControllerGame cg = new ControllerGame();
		cg.putGame(game);

		for(int i = 0; i < game.realDuration; i++)
		{
			//((Node) game.supplyChain[0]).currentStock -= game.demand[i];
			cg.makeOrder(game.gameID, null, 16);
			printSupplyChain(game.supplyChain);
			printSupplyChainProfit(game.supplyChain);
			
			for(Function value : Function.values())
			{
				cg.makeOrder(game.gameID, value, 16);
				System.out.print(i + " -" + 16 + ": ");
				printSupplyChain(game.supplyChain);
				printSupplyChainProfit(game.supplyChain);
			}
			System.out.println("");
		}
	}

	private void printSupplyChain(AbstractNode[] supplyChain)
	{
		Node node;

		for(AbstractNode an : supplyChain)
		{
			if(an instanceof TravellingTime)
				System.out.print("<" + an.travellingStock + "> -> ");
			else
			{
				node = (Node) an;

				System.out.print("<" + node.travellingStock + " | ");
				System.out.print(node.currentStock + "> -> ");
			}
		}
		System.out.println("");
	}
	
	private void printSupplyChainProfit(AbstractNode[] supplyChain)
	{
		Node node;

		for(AbstractNode an : supplyChain)
		{
			if(an instanceof Node)
			{
				node = (Node) an;

				System.out.print(node.profit + " -> ");
			}
		}
		System.out.println("");
	}
}
