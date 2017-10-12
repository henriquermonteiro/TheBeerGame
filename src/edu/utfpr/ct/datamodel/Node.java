package edu.utfpr.ct.datamodel;

import edu.utfpr.ct.interfaces.IFunction;
import java.util.List;

public class Node extends AbstractNode
{
	private static final long serialVersionUID = 1123038557054141213L;

	public String playerName;
	public Integer lastRequest;
	public IFunction function;
	public List<Integer> currentStock;
	public List<Integer> debt;
	public List<Double> costUnfulfillment;
	public List<Double> costStocking;
	public List<Double> profit;
	public List<Integer> playerMove;

	public int getLastStock()
	{
		return currentStock.isEmpty() ? -1 : currentStock.get(currentStock.size() - 1);
	}

	public double getLastUnfullfilmentCost()
	{
		return costUnfulfillment.isEmpty() ? -1 : costUnfulfillment.get(costUnfulfillment.size() - 1);
	}

	public double getLastStockingCost()
	{
		return costStocking.isEmpty() ? -1 : costStocking.get(costStocking.size() - 1);
	}

	public double getLastProfit()
	{
		return profit.isEmpty() ? -1 : profit.get(profit.size() - 1);
	}

	public int getLastDebt()
	{
		return debt.isEmpty() ? -1 : debt.get(debt.size() - 1);
	}
}
