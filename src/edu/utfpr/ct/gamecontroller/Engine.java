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

public class Engine
{
	public static final int SETUP = 1;
	public static final int RUNNING = 2;
	public static final int PAUSED = 4;
	public static final int FINISHED = 8;

	private final Set<String> players;
	private Game game;
	private Table table;
	private boolean clientTurn;
	private IFunction turn;
	private int weeks;
	private int state;

	public Engine()
	{
		this.players = new HashSet<>();
	}

	public Game getGame()
	{
		return game;
	}

	public void setGame(Game game, IFunction function)
	{
		this.game = game;
		this.turn = function;
		resetConfigs();
	}

	public boolean isClientTurn()
	{
		return clientTurn;
	}

	public IFunction getTurn()
	{
		return turn;
	}

	public int getWeeks()
	{
		return weeks;
	}

	public int getState()
	{
		return state;
	}

	public boolean setState(int state)
	{
		if(this.state == SETUP && ((state == RUNNING && isAllPlayerSet()) || (state == PAUSED)))
			this.state = state;
		else if(this.state == RUNNING)
			this.state = state;
		else if(this.state == PAUSED && state == RUNNING)
			this.state = state;

		if(this.state == PAUSED)
		{
			for(IFunction function : turn.getValues())
				removePlayerForNode(function);

			players.clear();
		}

		return (this.state == state);
	}

	public String[] getPlayers()
	{
		return players.toArray(new String[0]);
	}

	public String[] getPlayersOnPool()
	{
		Set<String> list = new HashSet<>(players);

		for(IFunction function : turn.getValues())
			list.remove(getNodeByFunction(function).playerName);

		return list.toArray(new String[0]);
	}

	public Table getTable()
	{
		if(table == null)
			table = new Table(game);

		return table;
	}

	public boolean addPlayer(String playerName)
	{
		return players.add(playerName);
	}

	public boolean removePlayer(String playerName)
	{
		for(IFunction function : turn.getValues())
			if(getNodeByFunction(function).playerName.equals(playerName))
			{
				getNodeByFunction(function).playerName = "";
				setState(Engine.SETUP); // Se falta jogadores o jogo não pode continuar;
			}

		return players.remove(playerName);
	}

	public boolean removePlayerForNode(IFunction function)
	{
		getNodeByFunction(function).playerName = "";
		setState(Engine.SETUP);

		return true;
	}

	//WHATAPORRA?
	public boolean changePlayerForNode(IFunction function, String playerName)
	{
		if(!"".equals(playerName) && !players.contains(playerName))
			return false;

		getNodeByFunction(function).playerName = playerName;
		if(isAllPlayerSet())
			setState(Engine.RUNNING);

		return true;
	}

	public boolean isAllPlayerSet()
	{
		for(IFunction function : turn.getValues())
			if("".equals(getNodeByFunction(function).playerName))
				return false;

		return true;
	}

	public boolean isPlayerSet(IFunction function)
	{
		return "".equals(getNodeByFunction(function).playerName);
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

		chainSize = turn.getValues().length;
		chainSize = chainSize + chainSize * game.deliveryDelay;

		if(game.timestamp == 0)
			game.timestamp = new GregorianCalendar().getTimeInMillis();
		if(game.demand == null)
			game.demand = new int[game.realDuration];

		game.supplyChain = new AbstractNode[chainSize];

		position = 0;
		for(IFunction value : turn.getValues())
		{
			node = new Node();
			node.currentStock = new ArrayList<>();
			node.profit = new ArrayList<>();
			node.playerMove = new ArrayList<>();

			node.travellingStock = 0;
			node.playerName = "";
			node.function = value;
			node.currentStock.add(game.initialStock);
			node.profit.add(0.0); //POR QUE ESTA PORRA ESTÁ AQUI?
			node.latsRequest = 0;
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

		state = RUNNING;

		for(int i = 0; i < game.realDuration; i++)
		{
			/* For the client turn */
			makeOrder(game.demand[i]);
//            nextTurn();

			while(!clientTurn)
			{
				node = getNodeByFunction(turn);

				if(i == node.playerMove.size())
				{
					state = SETUP;
					getTable().updateLines();
					return;
				}

				makeOrder(node.playerMove.get(i), false);
//                nextTurn();
			}
		}

		getTable().updateLines();

		state = FINISHED;
	}

	public int makeOrder(int order)
	{
		return makeOrder(order, true);
	}

	/**
	 * Makes all the order placed go from one Node to the other.
	 *
	 * @param order
	 * @param updateTable
	 * @return
	 */
	public int makeOrder(int order, boolean updateTable)
	{
		int posCurrentNode, qty;
		Node node;

		if(order < 0 || state != RUNNING)
			return -1;

		if(clientTurn)
			qty = makeOrderRecursion(0, order);
		else
		{
			posCurrentNode = (turn.getPosition() - 1) * game.deliveryDelay + (turn.getPosition() - 1);
			node = (Node) game.supplyChain[posCurrentNode];
			qty = makeOrderRecursion(posCurrentNode + 1, order);
			node.currentStock.add(node.getLastStock() + qty);
			if(updateTable)
				getTable().updateLines();
		}
		nextTurn();

		return qty;
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
			node.travellingStock = node.getLastStock() >= order ? order : node.getLastStock();
//			node.currentStock.add(node.getLastStock() >= order ? node.getLastStock() - order : 0);
			node.currentStock.set(node.currentStock.size() - 1, node.getLastStock() >= order ? node.getLastStock() - order : 0);
			node.latsRequest = order; // Save the amount requested for rendering purposes.

			return node.travellingStock;
		}

		return Integer.MIN_VALUE;
	}

	private void calculateProfit(Node node, int order)
	{
		double profit = node.getLastProfit();

		profit += node.getLastStock() >= order ? order * game.sellingUnitProfit : node.getLastStock() * game.sellingUnitProfit;
		/* Cost from missing unit */
		profit -= node.getLastStock() >= order ? 0 : (order - node.getLastStock()) * game.missingUnitCost;
		/* Cost from the remaining stock after selling */
		profit -= node.getLastStock() >= order ? (node.getLastStock() - order) * game.stockUnitCost : 0;

		node.profit.add(profit);
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
		clientTurn = true;
		turn = turn.first();
		weeks = 0;
		state = PAUSED;
	}
}
