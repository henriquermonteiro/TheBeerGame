package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.interfaces.IFunction;
import java.util.List;

public class Node extends AbstractNode
{
	private static final long serialVersionUID = 8650978713791502113L;

	public String playerName;
	public Integer latsRequest;
	public IFunction function;
	public List<Integer> currentStock;
	public List<Double> profit;
	public List<Integer> playerMove;

	public int getLastStock()
	{
		return currentStock.isEmpty() ? -1 : currentStock.get(currentStock.size() - 1);
	}

	public double getLastProfit()
	{
		return profit.isEmpty() ? -1 : profit.get(profit.size() - 1);
	}
}
