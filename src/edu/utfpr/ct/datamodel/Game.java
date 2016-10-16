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
	public int demand[];
	public boolean informedChainSupply;
	public int deliveryDelay;
	public int unitiesOnTravel[];
	public List<AbstractNode> supplyChain;
}
