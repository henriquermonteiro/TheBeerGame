package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.interfaces.IControllerHost2;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IFunction;
import edu.utfpr.ct.interfaces.ILogger2;
import edu.utfpr.ct.interfaces.IReport;
import edu.utfpr.ct.logmanager.Logger2;
import edu.utfpr.ct.report.ReportManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControllerHost implements IControllerHost2
{
	private final Map<String, Engine> engines;
	private final Set<Game> reports;
	private final IReport reportManager;
	private final ILogger2 logger;

	public ControllerHost()
	{
		this.engines = new HashMap<>();
		this.reports = new HashSet<>();
		this.reportManager = new ReportManager();
		this.logger = Logger2.getLogger();

		loadResources();
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

		reports.addAll(Arrays.asList(reportManager.getReports()));
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

		engines.entrySet().stream().forEach(
			(entry) -> 
				{
					unfinishedGames.add(entry.getValue().getGame());
			});

		return unfinishedGames.toArray(new Game[0]);
	}

	@Override
	public Game[] getReports()
	{
		return reports.toArray(new Game[0]);
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
	public Game getReport(String gameName)
	{
		for(Game game : reports)
			if(game.name == gameName)
				return game;

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
		for(Game report : reports)
			if(report.name == gameName)
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
			reports.add(engine.getGame());
		}

		return qty;
	}
}
