package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.interfaces.IFunction;
import edu.utfpr.ct.interfaces.IReport;
import edu.utfpr.ct.logmanager.Logger;
import edu.utfpr.ct.report.ReportManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.interfaces.IControllerPlayer2;
import edu.utfpr.ct.interfaces.ILogger;
import java.util.Arrays;

public class Controller implements IControllerHost, IControllerPlayer2
{
	private static Controller controller;
	private final Map<String, Engine> engines;
	private final Map<Game, Boolean> reports;
	private final IReport reportManager;
	private final ILogger logger;

	private Controller()
	{
		this.engines = new HashMap<>();
		this.reports = new HashMap<>();
		this.reportManager = new ReportManager();
		this.logger = Logger.getLogger();

		loadResources();
	}

	public synchronized static Controller getController()
	{
		if(controller == null)
			controller = new Controller();

		return controller;
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
				reportManager.createReport(game);
			else
				engines.put(game.name, engine);
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
		logger.logGameStart(game);
		engines.put(game.name, engine);

		return true;
	}

	@Override
	public Game[] getGames()
	{
		List<Game> unfinishedGames = new ArrayList<>();

		engines.entrySet().stream().forEach((entry) ->
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
		return engines.get(gameName).getPlayers();
	}

	@Override
	public Game getGame(String gameName)
	{
		return engines.get(gameName).getGame();
	}

	@Override
	public int getGameState(String gameName)
	{
		return engines.get(gameName).getState();
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
			if(entry.getKey().name.equals(gameName))
				return entry.getValue() ? entry.getKey() : null;

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
			if(entry.getKey().name.equals(gameName))
			{
				reportManager.purgeReport(entry.getKey());
				reports.remove(entry.getKey());
				break;
			}

		return true;
	}

	@Override
	public boolean startGame(String gameName)
	{
		return engines.get(gameName).setState(Engine.RUNNING);
	}

	@Override
	public boolean pauseGame(String gameName)
	{
		return engines.get(gameName).setState(Engine.PAUSED);
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
			engines.remove(gameName);
			logger.purgeGame(order);
			reportManager.createReport(engine.getGame());
			reports.putIfAbsent(engine.getGame(), false);
		}

		return qty;
	}

	/* IControllerPlayer2 */

	@Override
	public String checkIn(String playerName)
	{
		for(Engine engine : engines.values())
			if(Arrays.stream(engine.getPlayers()).anyMatch(playerName::equals))
				return engine.getGame().name;

		return ""; //null?
	}

	@Override
	public Integer postMove(String gameName, IFunction function, String playerName, Integer order)
	{
		try
		{
			Node node = engines.get(gameName).getNodeOfTurn();

			if(node.function == function && node.playerName.equals(playerName))
				return postMoveForNode(gameName, order);
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
		return getGames();
	}

	@Override
	public boolean enterGameRoom(String gameName, String playerName)
	{
		return engines.get(gameName).addPlayer(playerName);
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
	public Game getGameData(String gameName, String playerName)
	{
		Engine engine = engines.get(gameName);
		
		if(engine != null)
			if(Arrays.stream(engine.getPlayers()).anyMatch(s -> s.equals(playerName)))
				return engine.getGame();
		
		return null;
	}
}
