package edu.utfpr.ct.dataextractor;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Table {

    private final Game game;
//	private AbstractNode[] supplyChain;
//	private final List<Integer> currentWeek;
    private final List<Line> lines;

    public Table(Game game) {
        this.game = game;
//		this.currentWeek = new ArrayList<>();
        this.lines = new ArrayList<>();

//                this.supplyChain = game.supplyChain;
//                
//                for(AbstractNode abstractNode : supplyChain)
//                            if(!(abstractNode instanceof TravellingTime))
//                                    currentWeek.add(0);
    }

    public List<Line> getLines() {
        return lines;
    }

//        public void buildTable(){
//		this.supplyChain = game.supplyChain;
//                for(AbstractNode abstractNode : supplyChain)
//                            if(!(abstractNode instanceof TravellingTime))
//                                    currentWeek.add(0);
//        }
    public void updateLines() {
        Line line;
        Node node;

        int lastKnowWeek = -1;
        int lastPlayer = -1;

        if (lines.size() > 0) {
            lastKnowWeek = lines.get(lines.size() - 1).week;
            lastPlayer = lines.get(lines.size() - 1).function;
        }

        while (((Node) game.supplyChain[0]).playerMove.size() > lastKnowWeek + 1) {

            if (lastPlayer == -1) {
                line = new Line();
                line.function = 0;
                line.week = lastKnowWeek + 1;
                line.currentStock = null;
                line.profit = null;
                line.playerMove = game.demand[lastKnowWeek + 1];
                line.incomingOrder = new ArrayList<>(Collections.nCopies(game.deliveryDelay, null));

                lines.add(line);
            }

            while (true) {
                int pos = (lastPlayer + 1) * (game.deliveryDelay + 1);
                node = (Node) game.supplyChain[pos];

                if (node.playerMove.size() > lastKnowWeek + 1) {
                    line = new Line();
                    line.function = lastPlayer + 2;
                    line.week = lastKnowWeek + 1;
                    line.currentStock = node.currentStock.get(lastKnowWeek + 1);
                    line.profit = node.profit.get(lastKnowWeek + 1);
                    line.playerMove = node.playerMove.get(lastKnowWeek + 1);

                    line.incomingOrder = new ArrayList<>();

                    for (int k = 1; k <= game.deliveryDelay; k++) {
                        line.incomingOrder.add(game.supplyChain[pos + k].travellingStock);
                    }

                    lines.add(line);

                    lastPlayer++;

                    if (lastPlayer >= Function.PRODUCER.getPosition() - 1) {
                        lastPlayer = -1;
                        lastKnowWeek++;
                        break;
                    }
                } else {
                    return;
                }
            }
        }

//		for(int i = 0, j = 0; i < game.supplyChain.length; i += game.deliveryDelay + 1)
//		{
//			node = (Node) game.supplyChain[i];
////			if(node.playerMove.size() == currentWeek.get(j))
////				continue;
//
//			line = new Line();
//			line.function = node.function.getPosition();
//			line.week = node.playerMove.size() + 1;
////			line.currentStock = node.currentStock.get(currentWeek.get(j));
////			line.profit = node.profit.get(currentWeek.get(j));
////			line.playerMove = node.playerMove.get(currentWeek.get(j));
//			line.demand = node.travellingStock;
//
//			for(int k = 1; k <= game.deliveryDelay; k++)
//				line.order.add(game.supplyChain[i + k].travellingStock);
//
////			currentWeek.set(j, line.week);
//                        j++;
//		}
    }

    public class Line {

        public Integer function;
        public Integer week;
        public Integer currentStock;
        public Double profit;
        public Integer playerMove;
        public List<Integer> incomingOrder;

        public Line() {
            this.incomingOrder = new ArrayList<>();
        }
    }

    public List<Line> getNewLines(String playerName, int function, int week) {
        if (function < 0 || function > 4 || week < 0 || week > game.realDuration) {
            if (game.informedChainSupply) {
                return lines;
            }

            ArrayList<Line> filtered = new ArrayList<>();
            for (int k = 0; k < game.supplyChain.length; k += game.deliveryDelay + 1) {
                if (playerName.equals(((Node) game.supplyChain[k]).playerName)) {
                    for (int j = ((Node) game.supplyChain[k]).function.getPosition(); j < lines.size(); j += 5) {
                        filtered.add(lines.get(j));
                    }

                    return filtered;
                }
            }
        }

        ArrayList<Line> ret = new ArrayList<>();
        
        int func = -1;

        for (int k = 0; k < game.supplyChain.length; k += game.deliveryDelay + 1) {
            if (playerName.equals(((Node) game.supplyChain[k]).playerName)) {
                func = ((Node)game.supplyChain[k]).function.getPosition();
            }
        }

        for (Line l : lines) {
            if (week > l.week || (week == l.week && function >= l.function)) {
                continue;
            }

            if (game.informedChainSupply) {
                ret.add(l);
            } else {
                if(l.function == func){
                    ret.add(l);
                }
            }
        }

        return ret;
    }
}
