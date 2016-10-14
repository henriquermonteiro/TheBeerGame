package controller.configuration;

import controller.IConfiguration;
import java.util.LinkedList;
import java.util.List;
import model.Function;
import model.Game;
import model.INode;

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
