package test.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.Engine;
import java.util.Date;

public class PlaceOrderTest
{
	public void test()
	{
		Game game;

		game = new Game();
		game.timestamp = new Date().getTime();
		game.name = "GameTest";
		game.password = "PasswordTest";
		game.missingUnitCost = 1.0;
		game.stockUnitCost = 2.0;
		game.sellingUnitProfit = 3.0;
		game.realDuration = 36;
		game.informedDuration = 50;
		game.deliveryDelay = 2;
		game.unitiesOnTravel = 4;
		game.initialStock = 16;
		game.informedChainSupply = true;

		placeOrder(game);
	}

	private void placeOrder(Game game)
	{
		int order = 4;
		Engine engine = new Engine();
		engine.setGame(game, Function.RETAILER);
		engine.buildGame();
		engine.setState(Engine.RUNNING);

		for(int i = 0; i < game.realDuration; i++)
		{
			engine.makeOrder(order);
			printSupplyChain(game.supplyChain);
			printSupplyChainProfit(game.supplyChain);

			for(Function value : Function.values())
			{
				engine.makeOrder(order);
				System.out.print(i + " -" + order + ": ");
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
