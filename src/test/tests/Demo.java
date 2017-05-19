package test.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.ControllerHost;
import edu.utfpr.ct.interfaces.IControllerHost;

public class Demo
{
	private final IControllerHost ch;

	public Demo()
	{
		this.ch = ControllerHost.getControllerHost();
	}

	public void testLoggerOperations()
	{
		IControllerHost ch2;
		Comparator comparator = new Comparator();
		Game game, games[];

		game = generateGame();
		ch.createGame(game);
		generateDemand(game);
		ch.postMoveForNode(game.name, 4);
		ch.postMoveForNode(game.name, 8);
		ch.postMoveForNode(game.name, 16);

		ch2 = ControllerHost.getControllerHost();
		games = ch2.getGames();
		generateDemand(games[0]);

		comparator.compareAll(game, games[0]);
	}

	public void testGameOperations()
	{
		Game game, games[];

		game = generateGame();
		ch.createGame(game);
		generateDemand(game);

		try
		{
			ch.createGame(game);
			ch.createGame(game);
		}
		catch(Exception e1)
		{
			System.out.println("Demo::testGameOperations(): " + e1.getMessage());

			games = ch.getGames();
			System.out.println("Quantity of games before purge: " + games.length);

			ch.purgeGame(games[0].name);
			games = ch.getGames();
			System.out.println("Quantity of games after purge: " + games.length);

			//should accept
			ch.createGame(game);
			games = ch.getGames();
			System.out.println("Quantity of games after add: " + games.length);

			try
			{
				game = generateGame();
				game.name = "Test2";
				game.deliveryDelay = -2;
				ch.createGame(game);
			}
			catch(Exception e2)
			{
				System.out.println("Demo::testGameOperations(): " + e2.getMessage());
			}
		}
	}

	public void testPlayThrough()
	{
		Game reference, retrieved;
		Comparator comparator = new Comparator();

		try
		{
			reference = generateGame();
			ch.createGame(reference);
			generateDemand(reference);
			addUsers(reference);
			ch.startGame(reference.name);
			placeOrder(reference);

			retrieved = ch.getReport(reference.name);
			comparator.compareAll(retrieved, retrieved);
		}
		catch(Exception e)
		{
			System.out.println("Demo::testPlayThrough(): " + e.getMessage());
		}
	}

	private void placeOrder(Game game)
	{
		int order = 4;

		for(int i = 0; i < game.realDuration; i++)
		{
			ch.postMoveForNode(game.name, order);
			printSupplyChain(game.supplyChain);
			//printSupplyChainProfit(game.supplyChain);

			if(i == 5)
				ch.changePlayerForNode(game.name, Function.RETAILER, "Cróvis");

			for(Function value : Function.values())
			{
				ch.postMoveForNode(game.name, order);
				System.out.print(i + " -" + order + ": ");
				printSupplyChain(game.supplyChain);
				//printSupplyChainProfit(game.supplyChain);
			}
			System.out.println("");
		}
	}

	private void addUsers(Game game)
	{
		ch.addPlayerOnGame(game.name, "Jureg");
		ch.addPlayerOnGame(game.name, "Creito");
		ch.addPlayerOnGame(game.name, "Creuza");
		ch.addPlayerOnGame(game.name, "Cróvis");
		ch.addPlayerOnGame(game.name, "Arlindo");

		ch.changePlayerForNode(game.name, Function.RETAILER, "Creito");
		ch.changePlayerForNode(game.name, Function.WHOLESALER, "Jureg");
		ch.changePlayerForNode(game.name, Function.DISTRIBUTOR, "Creuza");
		ch.changePlayerForNode(game.name, Function.PRODUCER, "Arlindo");
	}

	private Game generateGame()
	{
		Game game;

		game = new Game();
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

		return game;
	}

	private void generateDemand(Game game)
	{
		for(int i = 0; i < game.demand.length; i++)
			game.demand[i] = 4;
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
				System.out.print(node.getLastStock() + "> -> ");
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

				System.out.print(node.getLastProfit() + " -> ");
			}
		}
		System.out.println("");
	}
}
