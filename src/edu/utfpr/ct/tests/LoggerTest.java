package edu.utfpr.ct.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.GameBuilder;
import edu.utfpr.ct.logmanager.Logger;
import java.util.ArrayList;
import java.util.Random;

public class LoggerTest
{
	private Game reference;
	private Game retrieved;

	public void test()
	{
		int qtd;
		Logger logger = new Logger();
		Comparator comparator = new Comparator();
		Game[] unfinishedGamesID;

		configureGame();
		logger.logGameStart(reference);
		fillPLayerMoves(logger);
		unfinishedGamesID = logger.getUnfinishedGamesID();
		retrieved = logger.retrieveGameData(unfinishedGamesID[0].gameID);
		comparator.compareAll(reference, retrieved);
		logger.purgeGame(unfinishedGamesID[0].gameID);

		qtd = logger.getUnfinishedGamesID().length;
		System.out.println("Qtd de jogos depois do purge: " + qtd);
	}

	private void configureGame()
	{
		GameBuilder gb = new GameBuilder();
		reference = new Game();

		reference.name = "Test";
		reference.password = "Test";
		reference.missingUnitCost = 0.5;
		reference.stockUnitCost = 1.0;
		reference.sellingUnitProfit = 1.0;
		reference.realDuration = 36;
		reference.informedDuration = 50;
		reference.deliveryDelay = 2;
		reference.unitiesOnTravel = 4;
		reference.initialStock = 16;
		reference.informedChainSupply = true;
		reference.demand = new int[36];

		for(int i = 0; i < reference.demand.length; i++)
		{
			if(i < 4)
				reference.demand[i] = 4;
			else
				reference.demand[i] = 8;
		}

		gb.buildGame(reference);
		fillSupplyChain();
	}

	private void fillSupplyChain()
	{
		int position;
		Node node;
		TravellingTime travellingTime;
		position = 0;
		for(Function value : Function.values())
		{
			node = new Node();
			node.playerName = "Test " + position;
			node.travellingStock = 0;
			node.currentStock = reference.initialStock;
			node.function = value;
			node.profit = Math.random() * 100;
			node.playerMove = new ArrayList<>();
			reference.supplyChain[position] = node;
			position++;

			for(int i = 0; i < reference.deliveryDelay; i++, position++)
			{
				travellingTime = new TravellingTime();
				travellingTime.travellingStock = reference.unitiesOnTravel;
				reference.supplyChain[position] = travellingTime;
			}
		}
	}

	private void fillPLayerMoves(Logger logger)
	{
		Node node;

		for(AbstractNode abstractNode : reference.supplyChain)
		{
			if(abstractNode instanceof TravellingTime)
				continue;

			node = (Node) abstractNode;

			for(int i = 0; i < node.playerMove.size(); i++)
			{
				node.playerMove.add(new Random().nextInt());
				logger.logPlayerMove(reference.gameID, node);
			}
		}
	}
}
