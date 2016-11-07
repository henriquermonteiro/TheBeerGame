package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.interfaces.IFunction;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

public class Wrapper
{
	public static final int SETUP = 1;
	public static final int RUNNING = 2;
	public static final int PAUSED = 4;
	public static final int FINISHED = 8;

	private final Set<String> players;
	private Game game;
	private boolean clientTurn;
	private IFunction turn;
	private int weeks;
	private int state;

	public Wrapper(IFunction function)
	{
		this.players = new HashSet<>();
		resetConfigs();
	}

	public Game getGame()
	{
		return game;
	}

	public void setGame(Game game)
	{
		resetConfigs();
		this.game = game;
	}

	public boolean isClientTurn()
	{
		return clientTurn;
	}

	public IFunction getTurn()
	{
		return turn;
	}

	public int getState()
	{
		return state;
	}

	public boolean setState(int state)
	{
		if((this.state == SETUP && state == RUNNING)
		   || (this.state == RUNNING && state == PAUSED)
		   || (this.state == PAUSED && state == RUNNING)
		   || state == FINISHED)
			this.state = state;

		return (this.state == state);
	}

	public String[] getPlayers()
	{
		return players.toArray(new String[0]);
	}

	public boolean addPlayer(String user)
	{
		return players.add(user);
	}

	public boolean removePlayer(String user)
	{
		return players.remove(user);
	}

	public boolean changePlayerForNode(IFunction function, String playerName)
	{
		Node node;

		node = getNodeByFunction(function);
		node.playerName = playerName;

		return true;
	}

	public boolean validateParameters(Game game)
	{
		return (game.timestamp >= 0
				&& !"".equals(game.name)
				&& game.missingUnitCost >= 0
				&& game.stockUnitCost >= 0
				&& game.sellingUnitProfit >= 0
				&& game.realDuration > 0
				&& game.informedDuration > 0
				&& game.deliveryDelay >= 0
				&& game.unitiesOnTravel >= 0
				&& game.initialStock >= 0
				&& game.realDuration <= game.informedDuration);
	}

	public void buildGame()
	{
		int chainSize, position;
		Node node;
		TravellingTime travellingTime;

		if(state != SETUP)
			return;

		chainSize = Function.values().length;
		chainSize = chainSize + chainSize * game.deliveryDelay;

		game.gameID = 0;
		game.timestamp = new GregorianCalendar().getTimeInMillis();
		game.demand = new int[game.realDuration];
		game.supplyChain = new AbstractNode[chainSize];

		position = 0;
		for(IFunction value : turn.getValues())
		{
			node = new Node();
			node.playerName = "";
			node.travellingStock = 0;
			node.currentStock = game.initialStock;
			node.function = value;
			node.profit = 0;
			node.playerMove = new ArrayList<>();
			game.supplyChain[position] = node;
			position++;

			for(int i = 0; i < game.deliveryDelay; i++, position++)
			{
				travellingTime = new TravellingTime();
				travellingTime.travellingStock = game.unitiesOnTravel;
				game.supplyChain[position] = travellingTime;
			}
		}
	}

	public void rebuildOrders()
	{
		Node node;

		if(state != SETUP)
			return;

		for(int i = 0; i < game.realDuration; i++)
		{
			/* For the client turn */
			makeOrder(game.demand[i]);
			nextTurn();

			while(!clientTurn)
			{
				node = getNodeByFunction(turn);
				makeOrder(node.playerMove.get(i));
				nextTurn();
			}
		}
	}

	/**
	 * Makes all the order placed go from one Node to the other.
	 *
	 * @param order
	 * @return
	 */
	public int makeOrder(int order)
	{
		int posCurrentNode, qtd;
		Node node;

		if(order < 0 || state != RUNNING)
			return -1;

		if(clientTurn)
			qtd = makeOrderRecursion(0, order);
		else
		{
			posCurrentNode = (turn.getPosition() - 1) * game.deliveryDelay + (turn.getPosition() - 1);
			node = (Node) game.supplyChain[posCurrentNode];
			qtd = makeOrderRecursion(posCurrentNode + 1, order);
			node.currentStock += qtd;
		}
		nextTurn();

		return qtd;
	}

	private int makeOrderRecursion(int posCurrentNode, int order)
	{
		int travellingStock;
		Node node;

		if(posCurrentNode == game.supplyChain.length)
			return order;

		if(game.supplyChain[posCurrentNode] instanceof TravellingTime)
		{
			travellingStock = game.supplyChain[posCurrentNode].travellingStock;
			game.supplyChain[posCurrentNode].travellingStock = makeOrderRecursion(posCurrentNode + 1, order);

			return travellingStock;
		}

		if(game.supplyChain[posCurrentNode] instanceof Node)
		{
			node = (Node) game.supplyChain[posCurrentNode];
			calculateProfit(node, order);
			node.travellingStock = node.currentStock >= order ? order : node.currentStock;
			node.currentStock = node.currentStock >= order ? node.currentStock - order : 0;

			return node.travellingStock;
		}

		return -1;
	}

	private void calculateProfit(Node node, int order)
	{
		/* Profit from selling order */
		node.profit += node.currentStock >= order ? order * game.sellingUnitProfit : node.currentStock * game.sellingUnitProfit;
		/* Cost from missing unit */
		node.profit -= node.currentStock >= order ? 0 : (order - node.currentStock) * game.missingUnitCost;
		/* Cost from the remaining stock after selling */
		node.profit -= node.currentStock >= order ? (node.currentStock - order) * game.stockUnitCost : 0;
	}

	private Node getNodeByFunction(IFunction function)
	{
		int posCurrentNode = (function.getPosition() - 1) * game.deliveryDelay + (function.getPosition() - 1);

		return (Node) game.supplyChain[posCurrentNode];
	}

	public Node getNodeOfTurn()
	{
		return clientTurn ? null : getNodeByFunction(turn);
	}

	private void nextTurn()
	{
		if(state == FINISHED)
			return;

		if(clientTurn)
		{
			turn = turn.first();
			clientTurn = false;
		}
		else if(turn.isLast())
		{
			clientTurn = true;
			weeks++;
		}
		else
			turn = turn.next();

		if(game.realDuration == weeks)
			setState(FINISHED);
	}

	private void resetConfigs()
	{
		players.clear();
		game = null;
		clientTurn = true;
		turn = turn.first();
		weeks = 0;
		state = SETUP;
	}
}
