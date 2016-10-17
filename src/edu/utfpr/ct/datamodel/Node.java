package edu.utfpr.ct.datamodel;

import java.util.List;

public class Node extends AbstractNode
{
	public String activePlayerID;
	public Function function;
	public int initialStock;
	public int currentStock;
	public double profit;
	public List<Integer> playerMove;
}
