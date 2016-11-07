package edu.utfpr.ct.datamodel;

import java.io.Serializable;

public class Game implements Serializable
{
	public int gameID;
	public long timestamp;
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
	public boolean informedChainSupply;
	public int[] demand;
	public AbstractNode[] supplyChain;
}
