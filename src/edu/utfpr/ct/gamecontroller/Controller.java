package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.hostgui.StartFrame;
import edu.utfpr.ct.interfaces.IFunction;
import edu.utfpr.ct.interfaces.IReport;
import edu.utfpr.ct.logmanager.Logger;
import edu.utfpr.ct.report.ReportManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.interfaces.ILogger;
import java.util.Arrays;
import edu.utfpr.ct.interfaces.IControllerPlayer;
import edu.utfpr.ct.webserver.ActionService;
import java.util.HashSet;
import java.util.function.Predicate;

public class Controller implements IControllerHost, IControllerPlayer
{
	private static Controller controller;
	private final Map<String, Engine> engines;
	private final Map<Game, Boolean> reports;
	private final IReport reportManager;
	private final ILogger logger;
	private final HashSet<String> players;
	private StartFrame hostGUI;

	private Controller()
	{
		this.engines = new HashMap<>();
		this.reports = new HashMap<>();
		this.reportManager = ReportManager.getReportManager();
		this.logger = Logger.getLogger();
		this.players = new HashSet<>();

		loadResources();
	}

	public synchronized static Controller getController()
	{
		if(controller == null)
			controller = new Controller();

		return controller;
	}

	public void setHostGUI(StartFrame hostGUI)
	{
		this.hostGUI = hostGUI;
	}

	@Override
	public void closeApplication()
	{
		ActionService.getService().stopService();
		logger.stopLogger();
	}

	private void loadResources()
	{
		Engine engine;

		for(Game game : logger.getGames())
		{
			engine = new Engine();
			engine.setGame(game, Function.RETAILER);
			engine.rebuildOrders();

			if(engine.getState() == Engine.FINISHED)
			{
				reportManager.createReport(engine.getTable());
				logger.purgeGame(game.gameID);
			}
			else
			{
				engines.put(game.name, engine);
				engine.setState(Engine.PAUSED);
			}
		}

		for(Game game : reportManager.getReports())
			reports.putIfAbsent(game, false);
	}

	@Override
	public boolean createGame(Game game) throws IllegalArgumentException
	{
		Engine engine = new Engine();

		if(!engine.validateParameters(game))
			throw new IllegalArgumentException("Invalid parameters");
		if(engines.containsKey(game.name))
			throw new IllegalArgumentException("Invalid parameter: name not unique");

		engine.setGame(game, Function.RETAILER);
		engine.buildGame();
		engine.rebuildOrders();
		engine.setState(Engine.PAUSED);

		logger.logGameStart(game);
		engines.put(game.name, engine);

		return true;
	}

	@Override
	public Game[] getGames()
	{
		List<Game> unfinishedGames = new ArrayList<>();
                
                for(Engine eng : engines.values()){
                    unfinishedGames.add(eng.getGame());
                }

		return unfinishedGames.toArray(new Game[0]);
	}

	@Override
	public Game[] getReports()
	{
		return reports.keySet().toArray(new Game[0]);
	}

	public String[] getPoolOfPlayers(String gameName)
	{
		return engines.get(gameName).getPlayersOnPool();
	}

	@Override
	public String[] getPlayersOnGame(String gameName)
	{
		Engine eng = engines.get(gameName);

		if(eng != null)
			return eng.getPlayersOnPool();

		return null;
	}

	@Override
	public Game getGame(String gameName)
	{
		Engine eng = engines.get(gameName);
		return (eng == null ? null : eng.getGame());
	}

	@Override
	public int getGameState(String gameName)
	{
		return (engines.get(gameName) == null ? (getReport(gameName) != null ? -1 : -2) : engines.get(gameName).getState());
	}

	@Override
	public int getReportState(String gameName)
	{
		Game report = getReport(gameName);

		return (report == null ? -1 : (reports.get(report) ? 8 : 16));
	}

	@Override
	public int getGameWeek(String gameName)
	{
		return engines.get(gameName).getWeeks();
	}

	@Override
	public Game getReport(String gameName)
	{
		for(Game report : reports.keySet())
			if(report.name.equals(gameName))
				return report;

		return null;
	}

	@Override
	public boolean purgeGame(String gameName)
	{
		Engine engine = engines.remove(gameName);
		logger.purgeGame(engine.getGame().gameID);

		return true;
	}

	@Override
	public boolean purgeReport(String gameName)
	{
		for(Game report : reports.keySet())
			if(report.name.equals(gameName))
			{
				reportManager.purgeReport(report);
				reports.remove(report);
				break;
			}

		return true;
	}

	@Override
	public boolean startGame(String gameName)
	{
		if(engines.get(gameName).setState(Engine.RUNNING))
			return true;
		return engines.get(gameName).setState(Engine.SETUP);
	}

	@Override
	public boolean pauseGame(String gameName)
	{
		if(engines.get(gameName).setState(Engine.PAUSED))
		{
			hostGUI.pushGameRoomUpdate(gameName);
			return true;
		}
		return false;
	}

	@Override
	public boolean startReport(String gameName)
	{
		for(Game report : reports.keySet())
			if(report.name.equals(gameName))
				return reports.put(report, true);

		return false;
	}

	@Override
	public boolean pauseReport(String gameName)
	{
		for(Game report : reports.keySet())
			if(report.name.equals(gameName))
				return reports.put(report, false);

		return false;
	}

	@Override
	public boolean addPlayerOnGame(String gameName, String playerName)
	{
		return engines.get(gameName).addPlayer(playerName);
	}

	@Override
	public boolean purgePlayerOnGame(String gameName, String playerName)
	{
		return engines.get(gameName).removePlayer(playerName);
	}

	@Override
	public boolean changePlayerForNode(String gameName, IFunction function, String playerName)
	{
		return engines.get(gameName).changePlayerForNode(function, playerName);
	}

	@Override
	public boolean removePlayerFromNode(String gameName, IFunction function)
	{
		return engines.get(gameName).removePlayerForNode(function);
	}

	@Override
	public Table getTable(String gameName)
	{
		return engines.get(gameName).getTable();
	}

	@Override
	public int postMoveForNode(String gameName, int order) throws IllegalStateException, IllegalArgumentException
	{
		Engine engine;
		int qty;

		if(!engines.containsKey(gameName))
			throw new IllegalStateException("Game not found");
		if(order < 0)
			throw new IllegalArgumentException("Invalid order");

		engine = engines.get(gameName);
		if(!engine.isClientTurn())
		{
			engine.getNodeOfTurn().playerMove.add(order);
			logger.logPlayerMove(engine.getGame().gameID, engine.getNodeOfTurn());
		}
		qty = engine.makeOrder(order);

		if(engine.getState() == Engine.FINISHED)
		{
			logger.purgeGame(engine.getGame().gameID);
			engines.remove(gameName);
			reportManager.createReport(engine.getTable());
			reports.putIfAbsent(engine.getGame(), true);
		}

		if(engine.isClientTurn() && engine.getWeeks() < engine.getGame().demand.length)
			engine.makeOrder(engine.getGame().demand[engine.getWeeks()]);

		hostGUI.pushGameRoomUpdate(gameName);

		return qty;
	}

	@Override
	public boolean isNameAvailable(String playerName)
	{
		return !players.contains(playerName);
	}

	@Override
	public boolean logout(String playerName)
	{
		for(Engine engine : engines.values())
			if(Arrays.asList(engine.getPlayers()).contains(playerName))
			{
				engine.removePlayer(playerName);
				hostGUI.pushGameRoomUpdate(engine.getGame().name);
				break;
			}

		return players.remove(playerName);
	}

	@Override
	public boolean checkIn(String playerName)
	{
		return players.add(playerName);
	}

	@Override
	public Integer postMove(String gameName, String playerName, Integer order)
	{
		Node node = engines.get(gameName).getNodeOfTurn();

		if(node.playerName.equals(playerName) && order >= 0 && order <= 1000)
			return postMoveForNode(gameName, order);

		return -1;
	}

	@Override
	public Game[] listAvailableGameRooms()
	{
		List<Game> unfinishedGames = new ArrayList<>();

		for(Engine engine : engines.values())
			if(engine.getState() != Engine.PAUSED)
				unfinishedGames.add(engine.getGame());

		for(Game report : reports.keySet())
			if(reports.get(report))
				unfinishedGames.add(report);

		return unfinishedGames.toArray(new Game[0]);
	}

	@Override
	public boolean enterGameRoom(String gameName, String playerName, String password)
	{
		if(!engines.containsKey(gameName))
			return getReport(gameName) != null;

		if(engines.get(gameName).getGame().password.equals(password))
		{       
                        boolean flag_break = false;
                        
                        for(Engine e : engines.values()){
                            if(!e.getGame().name.equals(gameName)){
                                for(String p : e.getPlayers()){
                                    if(p.equals(playerName)){
                                        e.removePlayer(playerName);
                                        flag_break = true;
                                        break;
                                    }
                                }
                                
                                if(flag_break){
                                    break;
                                }
                            }
                        }
                    
                        if(engines.get(gameName).addPlayer(playerName)){
                                hostGUI.pushGameRoomUpdate(gameName);
                                return true;
                        }
		}

		return false;
	}

	@Override
	public synchronized boolean selectPlayableNode(String gameName, IFunction function, String playerName)
	{
		Engine engine = engines.get(gameName);

		if(!engine.isPlayerSet(function))
			return engine.changePlayerForNode(function, playerName);

		return false;
	}

	@Override
	public EngineData getGameData(String gameName, String playerName)
	{
		Engine engine = engines.get(gameName);
                
                Predicate<String> pred = new Predicate<String>() {
                    @Override
                    public boolean test(String s) {
                        return s.equals(playerName);
                    }
                };

		if(engine != null)
			if(Arrays.stream(engine.getPlayers()).anyMatch(pred))
			{
				Game fullGame = engine.getGame();
				EngineData data = new EngineData();

				data.state = engine.getState();
				data.weeks = engine.getWeeks();
				data.turn = engine.getTurn();
				data.playerOfTurn = engine.getNodeOfTurn().playerName;
				data.game = new Game();

				data.game.gameID = fullGame.gameID;
				data.game.name = fullGame.name;
				data.game.informedDuration = fullGame.informedDuration;
				data.game.deliveryDelay = fullGame.deliveryDelay;
				data.game.stockUnitCost = fullGame.stockUnitCost;
				data.game.missingUnitCost = fullGame.missingUnitCost;
				data.game.sellingUnitProfit = fullGame.sellingUnitProfit;
                                data.game.initialStock = fullGame.initialStock;

				switch(data.state)
				{
				case Engine.SETUP:
					data.game.supplyChain = fullGame.supplyChain;

					break;
				case Engine.RUNNING:
//					if(!engine.getGame().informedChainSupply)
//					{
//						data.game.supplyChain = new AbstractNode[fullGame.supplyChain.length];
//
//						boolean flag = false;
//						for(int k = 0; k < fullGame.supplyChain.length; k++)
//							if(fullGame.supplyChain[k] instanceof Node)
//							{
//								Node realNode = (Node) fullGame.supplyChain[k];
//								flag = playerName.equals(realNode.playerName);
//
//								if(flag)
//									data.game.supplyChain[k] = realNode;
//								else
//								{
//									Node n = new Node();
//									n.function = realNode.function;
//									n.playerName = realNode.playerName;
//
//									data.game.supplyChain[k] = n;
//								}
//							}
//							else
//								data.game.supplyChain[k] = (flag ? fullGame.supplyChain[k] : null);
//
//					}
//					else
					data.game.supplyChain = fullGame.supplyChain;
                                        data.game.demand = fullGame.demand;
				}

				return data;
			}

		Game report = getReport(gameName);
		if(report != null)
			if(reports.get(report))
			{
				EngineData data = new EngineData();

				data.state = Engine.FINISHED;
				data.weeks = report.realDuration;
				data.turn = null;
				data.game = report;

				return data;
			}

		return null;
	}
}
