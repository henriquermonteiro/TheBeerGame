package edu.utfpr.ct.datamodel;

import java.util.List;

public class Node extends AbstractNode
{
	public String playerName;
	public Function function;
	public int initialStock; /*Tirar, foi pro Game*/
	public int currentStock;
	public double profit;
	public List<Integer> playerMove;
}
