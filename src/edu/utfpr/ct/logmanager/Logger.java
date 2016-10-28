package edu.utfpr.ct.logmanager;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.GameBuilder;
import edu.utfpr.ct.interfaces.ILogger;
import edu.utfpr.ct.logmanager.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Logger implements ILogger
{
	public Logger()
	{
		new Database().dropDB();
		new Database().initializeDB();
	}
	
	@Override
	public void logGameStart(Game game)
	{
		String query;
		PreparedStatement stmt;
		Node node;

		try
		{
			query = "INSERT INTO game(game_id, timestamp, name, password, missing_unit_cost, stock_unit_cost, selling_unit_profit, real_duration, informed_duration, delivery_delay, unities_on_travel, initial_stock, informed_chain_suplly) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = Database.getConnection().prepareStatement(query);
			stmt.setInt(1, game.gameID);
			stmt.setLong(2, game.timestamp);
			stmt.setString(3, game.name);
			stmt.setString(4, game.password);
			stmt.setDouble(5, game.missingUnitCost);
			stmt.setDouble(6, game.stockUnitCost);
			stmt.setDouble(7, game.sellingUnitProfit);
			stmt.setInt(8, game.realDuration);
			stmt.setInt(9, game.informedDuration);
			stmt.setInt(10, game.deliveryDelay);
			stmt.setInt(11, game.unitiesOnTravel);
			stmt.setInt(12, game.initialStock);
			stmt.setBoolean(13, game.informedChainSupply);
			stmt.execute();

			query = "INSERT INTO demand(game_id, week, value) "
					+ "VALUES (?, ?, ?)";

			for(int i = 0; i < game.demand.length; i++)
			{
				stmt = Database.getConnection().prepareStatement(query);
				stmt.setInt(1, game.gameID);
				stmt.setInt(2, i);
				stmt.setInt(3, game.demand[i]);
				stmt.execute();
			}

			query = "INSERT INTO supply_chain(game_id, node_position, player_name) "
					+ "VALUES (?, ?, ?)";

			for(AbstractNode supplyChain : game.supplyChain)
			{
				if(supplyChain instanceof TravellingTime)
					continue;

				node = (Node) supplyChain;
				stmt = Database.getConnection().prepareStatement(query);
				stmt.setInt(1, game.gameID);
				stmt.setInt(2, node.function.getPosition());
				stmt.setString(3, node.playerName);
				stmt.execute();
			}
		}
		catch(SQLException e)
		{
			System.out.println("Logger::logGameStart(Game game): " + e.getSQLState());
			System.out.println("Logger::logGameStart(Game game): " + e.getMessage());
		}
	}

	@Override
	public void logPlayerMove(Integer gameID, Integer nodeID, String playerName, Integer week, Integer move)
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}

	@Override
	public void logPlayerMove(int gameID, Node node)
	{
		String query;
		PreparedStatement stmt;

		try
		{
			query = "INSERT INTO moves (game_id, node_position, week, player_name, order_placed)"
					+ "VALUES (?, ?, ?, ?, ?)";

			stmt = Database.getConnection().prepareStatement(query);
			stmt.setInt(1, gameID);
			stmt.setInt(2, node.function.getPosition());
			stmt.setInt(3, node.playerMove.size());
			stmt.setString(4, node.playerName);
			stmt.setInt(5, node.playerMove.get(node.playerMove.size() - 1));
			stmt.execute();
		}
		catch(SQLException e)
		{
			System.out.println("Logger::logPlayerMove(int gameID, Node node): " + e.getSQLState());
			System.out.println("Logger::logPlayerMove(int gameID, Node node): " + e.getMessage());
		}
	}

	@Override
	public void purgeGame(Integer gameID)
	{
		String query;
		PreparedStatement stmt;

		try
		{
			query = "DELETE FROM moves WHERE game_id = " + gameID;
			stmt = Database.getConnection().prepareStatement(query);
			stmt.execute();

			query = "DELETE FROM supply_chain WHERE game_id = " + gameID;
			stmt = Database.getConnection().prepareStatement(query);
			stmt.execute();

			query = "DELETE FROM demand WHERE game_id = " + gameID;
			stmt = Database.getConnection().prepareStatement(query);
			stmt.execute();

			query = "DELETE FROM game WHERE game_id = " + gameID;
			stmt = Database.getConnection().prepareStatement(query);
			stmt.execute();
		}
		catch(SQLException e)
		{
			System.out.println("Logger::purgeGame(Integer gameID): " + e.getSQLState());
			System.out.println("Logger::purgeGame(Integer gameID): " + e.getMessage());
		}
	}

	@Override
	public Game[] getUnfinishedGamesID()
	{
		String query;
		ResultSet rs;
		Game game;
		List<Game> list;

		try
		{
			query = "SELECT game_id, timestamp, name FROM game";
			rs = Database.getConnection().prepareStatement(query).executeQuery();
			list = new ArrayList<>();

			while(rs.next())
			{
				game = new Game();
				game.gameID = rs.getInt(1);
				game.timestamp = rs.getLong(2);
				game.name = rs.getString(3);

				list.add(game);
			}

			return list.toArray(new Game[0]);
		}
		catch(SQLException e)
		{
			System.out.println("Logger::getUnfinishedGamesID(): " + e.getSQLState());
			System.out.println("Logger::getUnfinishedGamesID(): " + e.getMessage());
			return null;
		}
	}

	@Override
	public Game retrieveGameData(Integer gameID)
	{
		String query;
		ResultSet rs1, rs2;
		Game game;
		Node node;

		try
		{
			query = "SELECT * FROM game where game_id = " + gameID;
			rs1 = Database.getConnection().prepareStatement(query).executeQuery();
			rs1.next();

			game = new Game();
			game.gameID = rs1.getInt(1);
			game.timestamp = rs1.getLong(2);
			game.name = rs1.getString(3);
			game.password = rs1.getString(4);
			game.missingUnitCost = rs1.getDouble(5);
			game.stockUnitCost = rs1.getDouble(6);
			game.sellingUnitProfit = rs1.getDouble(7);
			game.realDuration = rs1.getInt(8);
			game.informedDuration = rs1.getInt(9);
			game.deliveryDelay = rs1.getInt(10);
			game.unitiesOnTravel = rs1.getInt(11);
			game.initialStock = rs1.getInt(12);
			game.informedChainSupply = rs1.getBoolean(13);

			query = "SELECT * FROM demand where game_id = " + gameID + " ORDER BY week";
			rs1 = Database.getConnection().prepareStatement(query).executeQuery();
			new GameBuilder().buildDemand(game);

			while(rs1.next())
				game.demand[rs1.getInt(2)] = rs1.getInt(3);

			query = "SELECT * FROM supply_chain where game_id = " + gameID + " ORDER BY node_position";
			rs1 = Database.getConnection().prepareStatement(query).executeQuery();
			new GameBuilder().buildSupplyChain(game);

			for(AbstractNode supplyChain : game.supplyChain)
			{
				if(supplyChain instanceof TravellingTime)
					continue;

				rs1.next();
				node = (Node) supplyChain;
				node.playerName = rs1.getString(3);

				query = "SELECT order_placed FROM moves where game_id = " + game.gameID + " and node_position = " + node.function.getPosition() + " ORDER BY week";
				rs2 = Database.getConnection().prepareStatement(query).executeQuery();

				while(rs2.next())
					node.playerMove.add(rs2.getInt(1));
			}

			return game;
		}
		catch(SQLException e)
		{
			System.out.println("Logger::retrieveGameData(Integer gameID): " + e.getSQLState());
			System.out.println("Logger::retrieveGameData(Integer gameID): " + e.getMessage());
			return null;
		}
	}
}
