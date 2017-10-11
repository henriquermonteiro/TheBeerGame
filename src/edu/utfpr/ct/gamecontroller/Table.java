package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table
{
	private final Game game;
	private final List<Line> lines;

	public Table(Game game)
	{
		this.game = game;
		this.lines = new ArrayList<>();
	}

	public List<Line> getLines()
	{
		return lines;
	}
        
        public void updateLines(){
            updateLines(game.realDuration);
        }

	public void updateLines(int stopWeek)
	{
		Line line;
		Node node;

		int lastKnowWeek = 0;
		int lastPlayer = -1;

                //Se já há registros, carrega o último;
		if(lines.size() > 0)
		{
			lastKnowWeek = lines.get(lines.size() - 1).week;
			lastPlayer = lines.get(lines.size() - 1).function;
                        
                        if(lastPlayer == 4){
                            lastPlayer = -1;
                            lastKnowWeek ++;
                        }
		}

                //Enquanto o primeiro jogador já fez uma jogada no turno atualmente averiguado
		while((((Node) game.supplyChain[0]).playerMove.size() > lastKnowWeek) && (lastKnowWeek != stopWeek))
		{
                        //Se o último jogador do turno é desconhecido, é a vez do consumidor
			if(lastPlayer == -1)
			{
				line = new Line();
				line.function = 0;
				line.week = lastKnowWeek;
				line.initialStock = null;
				line.orderReceived = null;
				line.orderPreviousPending = null;
				line.expectedDelivery = null;
				line.actualyDelivery = null;
				line.orderUnfullfiled = null;
				line.finalStock = null;
				line.playerMove = game.demand[lastKnowWeek];
				line.confirmedOrderDelivery = null;
				line.incomingOrder = new ArrayList<>(Collections.nCopies(game.deliveryDelay, null));
				line.costUnfulfillment = null;
				line.costStock = null;
				line.profit = null;
				line.weekBalance = null;

				lines.add(line);
                                
                                //Último jogador foi o consumidor
                                lastPlayer = 0;
			}

			while(true)
			{
//				int pos = (lastPlayer + 1) * (game.deliveryDelay + 1);
				int pos = lastPlayer * (game.deliveryDelay + 1);
				node = (Node) game.supplyChain[pos];

                                //Se próximo jogador esperado já fez uma jogada no turno averiguado; Senão encerra o método
				if(node.playerMove.size() > lastKnowWeek)
				{
					line = new Line();
					line.function = lastPlayer + 1;
					line.week = lastKnowWeek;
                                        line.actualyDelivery = game.supplyChain[pos].travellingStock;
                                        line.initialStock = (lastKnowWeek <= 0 ? game.initialStock : (node.currentStock.get(lastKnowWeek) + line.actualyDelivery));
                                        line.orderReceived = (pos <= 0 ? game.demand[lastKnowWeek] : ((Node)game.supplyChain[pos - (game.deliveryDelay + 1)]).playerMove.get(lastKnowWeek));
                                        line.orderPreviousPending = node.debt.get(lastKnowWeek);
                                        line.expectedDelivery = (pos <= 0 ? 0 : line.orderPreviousPending) + line.orderReceived;
                                        line.orderUnfullfiled = line.expectedDelivery - line.actualyDelivery;
                                        line.finalStock = node.currentStock.get(lastKnowWeek);
                                        line.playerMove = node.playerMove.get(lastKnowWeek);
                                        line.confirmedOrderDelivery = game.supplyChain[pos + game.deliveryDelay].travellingStock;

					line.incomingOrder = new ArrayList<>();
                                        
                                        if(game.deliveryDelay > 0)
                                                line.incomingOrder.add(node.currentStock.get(lastKnowWeek + 1) - node.currentStock.get(lastKnowWeek));
                                        
					for(int k = 1; k < game.deliveryDelay; k++)
						line.incomingOrder.add(game.supplyChain[pos + k].travellingStock);
                                        
                                        line.costUnfulfillment = node.costUnfulfillment.get(lastKnowWeek + 1);
                                        line.costStock = node.costStocking.get(lastKnowWeek + 1);
                                        line.profit = node.profit.get(lastKnowWeek + 1);
                                        line.weekBalance = (game.sellingUnitProfit == 0 ? line.costUnfulfillment + line.costStock : line.profit - (line.costUnfulfillment + line.costStock));

					lines.add(line);

					lastPlayer++;

					if(lastPlayer >= Function.PRODUCER.getPosition())
					{
						lastPlayer = -1;
						lastKnowWeek++;
						break;
					}
				}
				else
					return;
			}
		}
	}

	public List<Line> getNewLines(String playerName, int function, int week)
	{
		if(function < 0 || function > 4 || week < 0 || week > game.realDuration)
		{
			if(game.informedChainSupply)
				return lines;

			ArrayList<Line> filtered = new ArrayList<>();

			for(int k = 0; k < game.supplyChain.length; k += game.deliveryDelay + 1)
				if(playerName.equals(((Node) game.supplyChain[k]).playerName))
				{
					for(int j = ((Node) game.supplyChain[k]).function.getPosition(); j < lines.size(); j += 5)
						filtered.add(lines.get(j));

					return filtered;
				}
		}

		ArrayList<Line> ret = new ArrayList<>();

		int func = -1;

		for(int k = 0; k < game.supplyChain.length; k += game.deliveryDelay + 1)
			if(playerName.equals(((Node) game.supplyChain[k]).playerName))
				func = ((Node) game.supplyChain[k]).function.getPosition();

		for(Line l : lines)
		{
			if(week > l.week || (week == l.week && function >= l.function))
				continue;

			if(game.informedChainSupply)
				ret.add(l);
			else if(l.function == func)
				ret.add(l);
		}

		return ret;
	}

	public class Line
	{
		public Integer function;
		public Integer week;
		public Integer initialStock;
		public Integer orderReceived;
		public Integer orderPreviousPending;
		public Integer expectedDelivery;
		public Integer actualyDelivery;
		public Integer orderUnfullfiled;
		public Integer finalStock;
		public Integer playerMove;
		public Integer confirmedOrderDelivery;
		public List<Integer> incomingOrder;
		public Double costUnfulfillment;
		public Double costStock;
		public Double profit;
		public Double weekBalance;

		public Line()
		{
			this.incomingOrder = new ArrayList<>();
		}
	}
}
