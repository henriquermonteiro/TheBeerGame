package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IFunction;
import edu.utfpr.ct.interfaces.ILogger;
import edu.utfpr.ct.interfaces.IReport;
import edu.utfpr.ct.logmanager.Logger;
import edu.utfpr.ct.report.ReportManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ControllerHost
{
	private final Map<String, Wrapper> wrappers;
	//private final Map<String, Game> reports;
	private final Set<Game> reports;
	private final IReport reportManager;
	private final ILogger logger;

	public ControllerHost()
	{
		this.wrappers = new HashMap<>();
		//this.reports = new HashMap<>();
		this.reports = new HashSet<>();
		this.reportManager = new ReportManager();
		this.logger = Logger.getLogger();
	}

	public boolean createGame(Game game) throws IllegalArgumentException
	{
		Wrapper wrapper = new Wrapper(Function.RETAILER);

		if(!wrapper.validateParameters(game))
			throw new IllegalArgumentException("Invalid parameters");
		if(wrappers.containsKey(game.name))
			throw new IllegalArgumentException("Invalid parameter: name not unique");

		wrapper.setGame(game);
		wrapper.buildGame();
		logger.logGameStart(game);
		wrappers.put(game.name, wrapper);

		return true;
	}

	private void loadResources()
	{
		Wrapper wrapper;

		for(Game game : logger.getUnfinishedGames())
		{
			wrapper = new Wrapper(Function.RETAILER);
			wrapper.setGame(game);
			wrapper.rebuildOrders();

			if(wrapper.getState() == Wrapper.FINISHED)
				reportManager.generateReport(game);
			else
				wrappers.put(game.name, wrapper);
		}

		reports.addAll(Arrays.asList(reportManager.loadReports()));
	}

	public boolean restoreGame(Integer gameID)
	{
		Wrapper wrapper = new Wrapper(Function.RETAILER);
		Game game = logger.retrieveGameData(gameID);

		wrapper.setGame(game);
		wrapper.rebuildOrders();
		wrappers.put(game.name, wrapper);

		return true;
	}

	public boolean startGame(String gameName)
	{
		return wrappers.get(gameName).setState(Wrapper.RUNNING);
	}

	public boolean stopGame(String gameName)
	{
		return wrappers.get(gameName).setState(Wrapper.PAUSED);
	}

	public boolean purgeGame(String gameName)
	{
		Wrapper wrapper = wrappers.remove(gameName);
		reportManager.purgeReport(wrapper.getGame());
		logger.purgeGame(wrapper.getGame().gameID);

		return true;
	}

	public Game getGameReport(String gameName)
	{
		return wrappers.get(gameName).getGame();
	}

	public Game[] getUnfinishedGames()
	{
		return logger.getUnfinishedGamesID();
	}

	public String[] getAvailableReports()
	{
		Wrapper wrapper;
		List<String> names = new ArrayList<>();

		for(Game report : reportManager.loadReports())
		{
			wrapper = new Wrapper(Function.RETAILER);
			wrapper.setGame(report);
			wrapper.setState(Wrapper.FINISHED);
			names.add(wrapper.getGame().name);
		}

		return names.toArray(new String[0]);
	}

	public boolean purgeReport(String gameName)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	public boolean changePlayerForNode(String gameName, IFunction function, String playerName)
	{
		return wrappers.get(gameName).changePlayerForNode(function, playerName);
	}

	public boolean removePlayerFromNode(String gameName, IFunction function)
	{
		return changePlayerForNode(gameName, function, "");
	}

	public Integer postMoveForNode(String gameName, Integer order) throws IllegalStateException
	{
		Wrapper wrapper;

		if(order < 0)
			return -1;

		wrapper = wrappers.get(gameName);
		if(wrapper.getState() == Wrapper.FINISHED)
			throw new IllegalStateException("Game is finished");

		if(!wrapper.isClientTurn())
			logger.logPlayerMove(wrapper.getGame().gameID, wrapper.getNodeOfTurn());

		return wrapper.makeOrder(order);
	}

	public Game getGameRoomData(String gameName)
	{
		return wrappers.get(gameName).getGame();
	}
}
