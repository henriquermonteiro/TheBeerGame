package edu.utfpr.ct.datamodel;

import java.util.List;

public class Game
{
	public int gameID;
	public String name;
	public String password;
	public double missingUnitCost;
	public double stockUnitCost;
	public double sellingUnitProfit;
	public int realDuration;
	public int informedDuration;
	public boolean informedChainSupply;
	public int[] demand;
	public int deliveryDelay;
	public int unitiesOnTravel2;
	public int[] unitiesOnTravel;
	public Demand demand2;
	public List<AbstractNode> supplyChain;
}
