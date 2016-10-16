package edu.utfpr.ct.gamecontroller.config;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.IConfiguration;
import java.util.ArrayList;
import java.util.LinkedList;

public class DefaultConfiguration implements IConfiguration
{
	@Override
	public Game configure()
	{
		Game game;
		
		game = new Game();
		configureGame(game);
		configureSupplyChain(game);
		configureDemand(game);
		
		return game;
	}
	
	private void configureGame(Game game)
	{
		game.gameID = 0;
		game.name = "";
		game.password = "";
		game.missingUnitCost = 1;
		game.stockUnitCost = 0.5;
		game.sellingUnitProfit = 1;
		game.realDuration = 36;
		game.informedDuration = 50;
		game.demand = new int[36];
		game.informedChainSupply = false;
		game.deliveryDelay = 2;
		game.unitiesOnTravel = new int[8];
		game.supplyChain = new LinkedList<>();
	}
	
	private void configureSupplyChain(Game game)
	{
		Node node;
		TravellingTime travellingTime;
		
		node = new Node();
		node.travellingStock = 0;
		node.activePlayerID = "";
		node.function = Function.RETAILER;
		node.initialStock = 12;
		node.currentStock = 12;
		node.profit = 0;
		node.playerMove = new ArrayList();
		game.supplyChain.add(node);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[0];
		game.supplyChain.add(travellingTime);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[1];
		game.supplyChain.add(travellingTime);
		
		node = new Node();
		node.travellingStock = 0;
		node.activePlayerID = "";
		node.function = Function.WHOLESALER;
		node.initialStock = 12;
		node.currentStock = 12;
		node.profit = 0;
		node.playerMove = new ArrayList();
		game.supplyChain.add(node);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[2];
		game.supplyChain.add(travellingTime);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[3];
		game.supplyChain.add(travellingTime);
		
		node = new Node();
		node.travellingStock = 0;
		node.activePlayerID = "";
		node.function = Function.DISTRIBUTOR;
		node.initialStock = 12;
		node.currentStock = 12;
		node.profit = 0;
		node.playerMove = new ArrayList();
		game.supplyChain.add(node);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[4];
		game.supplyChain.add(travellingTime);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[5];
		game.supplyChain.add(travellingTime);
		
		node = new Node();
		node.travellingStock = 0;
		node.activePlayerID = "";
		node.function = Function.PRODUCER;
		node.initialStock = 12;
		node.currentStock = 12;
		node.profit = 0;
		node.playerMove = new ArrayList();
		game.supplyChain.add(node);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[6];
		game.supplyChain.add(travellingTime);
		
		travellingTime = new TravellingTime();
		travellingTime.travellingStock = game.unitiesOnTravel[7];
		game.supplyChain.add(travellingTime);
	}

	private void configureDemand(Game game)
	{
		int i;
		
		for(i = 0; i < game.realDuration; i++)
			game.demand[i] = 4;
		
		for(; i < game.realDuration; i++)
			game.demand[i] = 8;
	}
}
