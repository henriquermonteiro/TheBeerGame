package edu.utfpr.ct.datamodel;

public class Game2
{
	public int gameID;
	public String name;
	public String password;
	public double missingUnitCost;
	public double stockUnitCost;
	public double sellingUnitProfit;
	public int realDuration;
	public int informedDuration;
	public int deliveryDelay;
	public int unitiesOnTravel;
	public int initialStock;
	public int[] demand;
	public AbstractNode[] supplyChain;
	public boolean informedChainSupply;
}
