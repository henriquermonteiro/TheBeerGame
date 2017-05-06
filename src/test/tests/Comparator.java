package test.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.util.List;
import java.util.Objects;

public class Comparator
{
	public void compareAll(Game game1, Game game2)
	{
		gameComparator(game1, game2);
		demandComparator(game1.demand, game2.demand);
		nodeComparator(game1.supplyChain, game2.supplyChain);
	}

	public void gameComparator(Game game1, Game game2)
	{
		if(game1.gameID != game2.gameID)
			System.out.println("game1.gameID = " + game1.gameID + ", game2.gameID = " + game2.gameID);
		if(game1.timestamp != game2.timestamp)
			System.out.println("game1.timestamp = " + game1.timestamp + ", game2.timestamp = " + game2.timestamp);
		if(!game1.name.equals(game2.name))
			System.out.println("game1.name = " + game1.name + ", game2.name = " + game2.name);
		if(!game1.password.equals(game2.password))
			System.out.println("game1.password = " + game1.password + ", game2.password = " + game2.password);
		if(game1.missingUnitCost != game2.missingUnitCost)
			System.out.println("game1.missingUnitCost = " + game1.missingUnitCost + ", game2.missingUnitCost = " + game2.missingUnitCost);
		if(game1.stockUnitCost != game2.stockUnitCost)
			System.out.println("game1.stockUnitCost = " + game1.stockUnitCost + ", game2.stockUnitCost = " + game2.stockUnitCost);
		if(game1.sellingUnitProfit != game2.sellingUnitProfit)
			System.out.println("game1.sellingUnitProfit = " + game1.sellingUnitProfit + ", game2.sellingUnitProfit = " + game2.sellingUnitProfit);
		if(game1.realDuration != game2.realDuration)
			System.out.println("game1.realDuration = " + game1.realDuration + ", game2.realDuration = " + game2.realDuration);
		if(game1.informedDuration != game2.informedDuration)
			System.out.println("game1.informedDuration = " + game1.informedDuration + ", game2.informedDuration = " + game2.informedDuration);
		if(game1.deliveryDelay != game2.deliveryDelay)
			System.out.println("game1.deliveryDelay = " + game1.deliveryDelay + ", game2.deliveryDelay = " + game2.deliveryDelay);
		if(game1.unitiesOnTravel != game2.unitiesOnTravel)
			System.out.println("game1.unitiesOnTravel = " + game1.unitiesOnTravel + ", game2.unitiesOnTravel = " + game2.unitiesOnTravel);
		if(game1.initialStock != game2.initialStock)
			System.out.println("game1.initialStock = " + game1.initialStock + ", game2.initialStock = " + game2.initialStock);
		if(game1.informedChainSupply != game2.informedChainSupply)
			System.out.println("game1.informedChainSupply = " + game1.informedChainSupply + ", game2.informedChainSupply = " + game2.informedChainSupply);
	}

	public void demandComparator(int[] demand1, int[] demand2)
	{
		if(demand1.length != demand2.length)
		{
			System.out.println("demand1.length = " + demand1.length + ", demand2.length = " + demand2.length);
			return;
		}

		for(int i = 0; i < demand1.length; i++)
			if(demand1[i] != demand2[i])
				System.out.println("demand1[" + i + "] = " + demand1[i] + ", demand2[" + i + "] = " + demand2[i]);
	}

	public void nodeComparator(AbstractNode[] supplyChain1, AbstractNode[] supplyChain2)
	{
		Node node1, node2;

		if(supplyChain1.length != supplyChain2.length)
		{
			System.out.println("supplyChain1.length = " + supplyChain1.length + ", supplyChain2.length = " + supplyChain2.length);
			return;
		}

		for(int i = 0; i < supplyChain1.length; i++)
		{
			if(supplyChain1[i] instanceof TravellingTime && supplyChain2[i] instanceof TravellingTime)
			{
				if(supplyChain1[i].travellingStock != supplyChain2[i].travellingStock)
					System.out.println("supplyChain1[" + i + "].travellingStock = " + supplyChain1[i].travellingStock + ", supplyChain2[" + i + "].travellingStock = " + supplyChain2[i].travellingStock);

				continue;
			}
			if(supplyChain1[i] instanceof Node && supplyChain2[i] instanceof Node)
			{
				node1 = (Node) supplyChain1[i];
				node2 = (Node) supplyChain2[i];

				if(!node1.playerName.equals(node2.playerName))
					System.out.println("supplyChain1[" + i + "].playerName = " + node1.playerName + ", supplyChain2[" + i + "].playerName = " + node2.playerName);
				if(node1.function != node2.function)
					System.out.println("supplyChain1[" + i + "].function = " + node1.function + ", supplyChain2[" + i + "].function = " + node2.function);
				if(node1.currentStock != node2.currentStock)
					System.out.println("supplyChain1[" + i + "].currentStock = " + node1.currentStock + ", supplyChain2[" + i + "].currentStock = " + node2.currentStock);
				if(node1.profit != node2.profit)
					System.out.println("supplyChain1[" + i + "].profit = " + node1.profit + ", supplyChain2[" + i + "].profit = " + node2.profit);

				playerMoveComparator(node1.playerMove, node2.playerMove);

				continue;
			}

			System.out.println("supplyChain1[" + i + "] = " + supplyChain1[i] + ", supplyChain2[" + i + "] = " + supplyChain2[i]);
		}
	}

	public void playerMoveComparator(List<Integer> playerMove1, List<Integer> playerMove2)
	{
		if(playerMove1.size() != playerMove2.size())
		{
			System.out.println("playerMove1.size() = " + playerMove1.size() + ", playerMove2.size() = " + playerMove2.size());
			return;
		}

		for(int i = 0; i < playerMove1.size(); i++)
			if(!Objects.equals(playerMove1.get(i), playerMove2.get(i)))
				System.out.println("playerMove1.get(" + i + ") = " + playerMove1.get(i) + ", playerMove2.get(" + i + ") = " + playerMove2.get(i));
	}
}
