package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.interfaces.IFunction;
import java.util.List;

public class Node extends AbstractNode
{
	private static final long serialVersionUID = -8635726000488404463L;

	public String playerName;
	public IFunction function;
	public int currentStock;
	public double profit;
	public List<Integer> playerMove;
}
