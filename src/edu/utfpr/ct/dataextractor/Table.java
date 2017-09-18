package edu.utfpr.ct.dataextractor;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.interfaces.IFunction;
import java.util.ArrayList;
import java.util.List;

public class Table
{
	private final Game game;
	private final AbstractNode[] supplyChain;
	private final List<Integer> currentWeek;
	private final List<Line> lines;

	public Table(Game game)
	{
		this.game = game;
		this.currentWeek = new ArrayList<>();
		this.supplyChain = game.supplyChain;
		this.lines = new ArrayList<>();

		for(AbstractNode abstractNode : supplyChain)
			if(!(abstractNode instanceof TravellingTime))
				currentWeek.add(0);
	}

	public List<Line> getLines()
	{
		return lines;
	}

	public void updateLines()
	{
		Line line;
		Node node;

		for(int i = 0, j = 0; i < supplyChain.length; i += game.unitiesOnTravel)
		{
			node = (Node) supplyChain[i];
			if(node.playerMove.size() == currentWeek.get(j))
				continue;

			line = new Line();
			line.function = node.function;
			line.week = node.playerMove.size() + 1;
			line.currentStock = node.currentStock.get(currentWeek.get(j));
			line.profit = node.profit.get(currentWeek.get(j));
			line.playerMove = node.playerMove.get(currentWeek.get(j));
			line.demand = node.travellingStock;

			for(int k = 0; k < game.unitiesOnTravel; k++)
				line.order.add(supplyChain[i + k].travellingStock);

			currentWeek.set(j, line.week);
		}
	}

	public class Line
	{
		public IFunction function;
		public Integer week;
		public Integer currentStock;
		public Double profit;
		public Integer playerMove;
		public List<Integer> order;
		public Integer demand;

		public Line()
		{
			this.order = new ArrayList<>();
		}
	}
        
        public List<Line> getNewLines(int function, int week){
            return null; //TODO
        }
}
