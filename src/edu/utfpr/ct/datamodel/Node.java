package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.interfaces.IFunction;
import java.util.List;

public class Node extends AbstractNode
{
	public String playerName;
	public IFunction function;
	public int currentStock;
	public double profit;
	public List<Integer> playerMove;
}
