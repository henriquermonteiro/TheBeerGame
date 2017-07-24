package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.interfaces.IFunction;
import java.util.List;

public class Node extends AbstractNode
{
	private static final long serialVersionUID = -8635726000488404463L;

	public String playerName;
	public IFunction function;
	public List<Integer> currentStock;
	public List<Double> profit;
	public List<Integer> playerMove;
        public Integer latsRequest;

	public int getLastStock()
	{
		if(currentStock.isEmpty())
			return -1;
		else
			return currentStock.get(currentStock.size() - 1);
	}

	public double getLastProfit()
	{
		if(profit.isEmpty())
			return -1;
		else
			return profit.get(profit.size() - 1);
	}
}
