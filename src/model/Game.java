package model;

import java.util.List;

public class Game
{
	public int gameID;
	public String name;
	public String password;
	public int missingUnitCost;
	public int stockUnitCost;
	public int sellingUnitProfit;
	public int realDuration;
	public int informedDuration;
	public int demand[];
	public boolean informedChainSupply;
	public int deliveryDelay;
	public int unitiesOnTravel[];
	public List<Integer> supplyChain;
}
