package model;

import java.util.List;

public class Node extends INode
{
	public String activePlayerID;
	public Function function;
	public String playerName;
	public int initialStock;
	public int currentStock;
	public int profit;
	public List playerMove;
	
	@Override
	public void removeStock(int quantity)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
	
	
}
