package edu.utfpr.ct.gamecontroller.config;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.INode;
import edu.utfpr.ct.gamecontroller.IConfiguration;
import java.util.LinkedList;

public class DefaultConfiguration implements IConfiguration
{
	private Game game;
	private INode node;
	
	@Override
	public void configure()
	{
		game.gameID = 0;
		game.name = "";
		game.password = "";
		game.missingUnitCost = 1;
		game.stockUnitCost = 1 /*0.5*/;
		game.sellingUnitProfit = 1;
		game.realDuration = 36;
		game.informedDuration = 50;
		game.demand = new int[36];
		game.informedChainSupply = false;
		game.deliveryDelay = 2;
		game.unitiesOnTravel = new int[15];
		game.supplyChain = new LinkedList<>();
	}
	
	public void defaultConfiguration()
	{

	}
	
	/*public void defaultConfiguration2()
	{
		public String activePlayerID = 0;
		public Function function;
		public String playerName;
		public int initialStock;
		public int currentStock;
		public int profit;
		public List playerMove;
	}*/
}
