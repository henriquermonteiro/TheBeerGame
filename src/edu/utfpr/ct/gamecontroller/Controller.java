package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.dataextractor.DataExtractor;
import edu.utfpr.ct.dataextractor.Table;
import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
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

public class Controller implements IControllerHost, IControllerPlayer
{
	private static Controller controller;
	private final Map<String, Engine> engines;
	private final Map<Game, Boolean> reports;
	private final IReport reportManager;
	private final ILogger logger;
	private final DataExtractor dataExtractor;

	private StartFrame hostGUI;

	private Controller()
	{
		this.engines = new HashMap<>();
		this.reports = new HashMap<>();
		this.reportManager = new ReportManager();
		this.logger = Logger.getLogger();
		this.dataExtractor = DataExtractor.getDataExtractor();

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
				reportManager.createReport(game);
				logger.purgeGame(game.gameID);
			}
			else
			{
				engines.put(game.name, engine);

				engine.setState(Engine.PAUSED);
			}
		}

//        Engine builder = new Engine();
		for(Game game : reportManager.getReports())
		{
//            builder.setGame(game, Function.RETAILER, false);
//            builder.rebuildOrders();
//            reports.putIfAbsent(builder.getGame(), false);
			reports.putIfAbsent(game, false);
		}
	}

	@Override
	public boolean createGame(Game game) throws IllegalArgumentException
	{
		Engine engine = new Engine();

		if(!engine.validateParameters(game))
		{
			throw new IllegalArgumentException("Invalid parameters");
		}
		if(engines.containsKey(game.name))
		{
			throw new IllegalArgumentException("Invalid parameter: name not unique");
		}

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

//        engines.values().stream().filter((eng) -> (eng.getState() == Engine.SETUP || eng.getState() == Engine.RUNNING || eng.getState() == Engine.FINISHED)).forEachOrdered((eng) -> {
//            unfinishedGames.add(eng.getGame());
//        });
		engines.entrySet().stream().forEach((entry)
			->
		{
			unfinishedGames.add(entry.getValue().getGame());
		});

		return unfinishedGames.toArray(new Game[0]);
	}

	@Override
	public Game[] getReports()
	{
		return reports.keySet().toArray(new Game[0]);
	}

	@Override
	public String[] getPlayersOnGame(String gameName)
	{
//        return engines.get(gameName).getPlayers();
		return engines.get(gameName).getPlayersOnPool();
	}

	@Override
	public Game getGame(String gameName)
	{
		return engines.get(gameName).getGame();
	}

	@Override
	public int getGameState(String gameName)
	{
		return (engines.get(gameName) == null ? (getReport(gameName) != null ? -1 : -2) : engines.get(gameName).getState());
	}

	@Override
	public int getReportState(String gameName)
	{
		Game g = getReport(gameName);
		return (g == null ? -1 : (reports.get(g) ? 8 : 16));
	}

	@Override
	public int getGameWeek(String gameName)
	{
		return engines.get(gameName).getWeeks();
	}

	@Override
	public Game getReport(String gameName)
	{
		for(Map.Entry<Game, Boolean> entry : reports.entrySet())
		{
			if(entry.getKey().name.equals(gameName))
			{
//                return entry.getValue() ? entry.getKey() : null;
				return entry.getKey();
			}
		}

		return null;
	}

	@Override
	public boolean purgeGame(String gameName)
	{
		Engine engine = engines.remove(gameName);
		reportManager.purgeReport(engine.getGame());
		logger.purgeGame(engine.getGame().gameID);

		return true;
	}

	@Override
	public boolean purgeReport(String gameName)
	{
		for(Map.Entry<Game, Boolean> entry : reports.entrySet())
		{
			if(entry.getKey().name.equals(gameName))
			{
				reportManager.purgeReport(entry.getKey());
				reports.remove(entry.getKey());
				break;
			}
		}

		return true;
	}

	@Override
	public boolean startGame(String gameName)
	{
		if(engines.containsKey(gameName))
		{
			Game g = engines.get(gameName).getGame();
			for(int k = 0; k < g.supplyChain.length; k += g.deliveryDelay + 1)
			{
				if(((Node) g.supplyChain[k]).playerName == null || ((Node) g.supplyChain[k]).playerName.equals(""))
				{
					return engines.get(gameName).setState(Engine.SETUP);
				}
			}
		}

		return engines.get(gameName).setState(Engine.RUNNING);
	}

	@Override
	public boolean pauseGame(String gameName)
	{
		return engines.get(gameName).setState(Engine.PAUSED);
	}

	@Override
	public boolean startReport(String gameName)
	{
		Game game = null;

		for(Game g : reports.keySet())
		{
			if(g.name.equals(gameName))
			{
				game = g;
				break;
			}
		}

		if(game != null)
		{
			reports.put(game, true);
		}

		return game != null;
	}

	@Override
	public boolean pauseReport(String gameName)
	{
		Game game = null;

		for(Game g : reports.keySet())
		{
			if(g.name.equals(gameName))
			{
				game = g;
				break;
			}
		}

		if(game != null)
		{
			reports.put(game, false);
		}

		return game != null;
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
		return engines.get(gameName).changePlayerForNode(function, "");
	}
        
        @Override
        public Table getTableData(String gameName){
            return dataExtractor.getTable(gameName);
        }
	
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
			reportManager.createReport(engine.getGame());
			reports.putIfAbsent(engine.getGame(), false);
		}

		if(engine.isClientTurn())
			engine.makeOrder(engine.getGame().demand[engine.getWeeks() - 1]);

		return qty;
	}

	/* IControllerPlayer2 */
	@Override
	public String checkIn(String playerName)
	{
		for(Engine engine : engines.values())
		{
			if(Arrays.stream(engine.getPlayers()).anyMatch(playerName::equals))
			{
				return engine.getGame().name;
			}
		}

		return ""; //null?
	}

	@Override
	public Integer postMove(String gameName, String playerName, Integer order)
	{
		try
		{
			Node node = engines.get(gameName).getNodeOfTurn();

//            if (node.function == function && node.playerName.equals(playerName)) {
			if(node.playerName.equals(playerName))
			{
				return postMoveForNode(gameName, order);
			}
		}
		catch(Exception e)
		{
			System.out.println("Controller::postMove(String gameName, IFunction function, String playerName, Integer order): " + e);
		}

		return -1;
	}

	/* Pra que eu preciso do nome do usuário? Se for só apra ele ler a lista de 
	jogos disponíveis é indiferente eu ter ou não o nome */
	@Override
	public Game[] listAvailableGameRooms(String playerName)
	{
//        return getGames();
		List<Game> unfinishedGames = new ArrayList<>();

		engines.values().stream().filter((eng) -> (eng.getState() == Engine.SETUP || eng.getState() == Engine.RUNNING || eng.getState() == Engine.FINISHED)).forEachOrdered((eng) ->
		{
			unfinishedGames.add(eng.getGame());
		});

		for(Game g : reports.keySet())
		{
			if(reports.get(g))
			{
				unfinishedGames.add(g);
			}
		}

		return unfinishedGames.toArray(new Game[0]);
	}

	@Override
	public boolean enterGameRoom(String gameName, String playerName, String password)
	{
		Engine engine = engines.get(gameName);

		if(engine == null)
		{
			Game g = getReport(gameName);

			return g != null;
		}

		if(engine.getGame().password.equals(password))
		{
			if(engines.get(gameName).addPlayer(playerName))
			{
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
		{
			return engine.changePlayerForNode(function, playerName);
		}

		return false;
	}

	@Override
	public EngineData getGameData(String gameName, String playerName)
	{
		Engine engine = engines.get(gameName);

		if(engine != null)
		{
			if(Arrays.stream(engine.getPlayers()).anyMatch(s -> s.equals(playerName)))
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

				switch(data.state)
				{
				case Engine.SETUP:
					data.game.supplyChain = fullGame.supplyChain;

					break;
				case Engine.RUNNING:
					if(!engine.getGame().informedChainSupply)
					{
						data.game.supplyChain = new AbstractNode[fullGame.supplyChain.length];

						boolean flag = false;
						for(int k = 0; k < fullGame.supplyChain.length; k++)
						{
							if(fullGame.supplyChain[k] instanceof Node)
							{
								Node realNode = (Node) fullGame.supplyChain[k];
								flag = playerName.equals(realNode.playerName);

								if(flag)
								{
									data.game.supplyChain[k] = realNode;
								}
								else
								{
									Node n = new Node();
									n.function = realNode.function;
									n.playerName = realNode.playerName;

									data.game.supplyChain[k] = n;
								}
							}
							else
							{
								data.game.supplyChain[k] = (flag ? fullGame.supplyChain[k] : null);
							}
						}

					}
					else
					{
						data.game.supplyChain = fullGame.supplyChain;
					}
				}

				return data;
			}
		}

		Game report = getReport(gameName);
		if(report != null)
		{
			if(reports.get(report))
			{
				EngineData data = new EngineData();

				data.state = Engine.FINISHED;
				data.weeks = report.realDuration;
				data.turn = null;
				data.game = report;

				return data;
			}
		}

		return null;
	}
}
