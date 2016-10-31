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

		placeOrder(game);
	}

	private void placeOrder(Game game)
	{
		ControllerGame cg = new ControllerGame();
		cg.putGame(game);

		for(int i = 0; i < game.realDuration; i++)
		{
			((Node) game.supplyChain[0]).currentStock -= game.demand[i];
			printSupplyChain(game.supplyChain);
			
			for(Function value : Function.values())
			{
				cg.makeOrder(game.gameID, value, game.demand[i]);
				System.out.print(i + " -" + game.demand[i] + ": ");
				printSupplyChain(game.supplyChain);
			}
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
}