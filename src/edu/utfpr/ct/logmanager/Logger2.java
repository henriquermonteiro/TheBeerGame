package edu.utfpr.ct.logmanager;

import edu.utfpr.ct.interfaces.ILogger2;
import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.gamecontroller.Engine;
import edu.utfpr.ct.logmanager.database.Database;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Logger2 implements ILogger2
{
	private static Logger2 logger;

	private Logger2()
	{
//		new Database().dropDB();
		new Database().initializeDB();
	}

	public static Logger2 getLogger()
	{
		if(logger == null)
			logger = new Logger2();

		return logger;
	}

	@Override
	public void logGameStart(Game game)
	{
		String query;
		PreparedStatement stmt;
		ResultSet rs;
		Node node;

		try
		{
			query = "INSERT INTO game(timestamp, name, password, missing_unit_cost, stock_unit_cost, selling_unit_profit, real_duration, informed_duration, delivery_delay, unities_on_travel, initial_stock, informed_chain_suplly) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = Database.getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			stmt.setLong(1, game.timestamp);
			stmt.setString(2, game.name);
			stmt.setString(3, game.password);
			stmt.setDouble(4, game.missingUnitCost);
			stmt.setDouble(5, game.stockUnitCost);
			stmt.setDouble(6, game.sellingUnitProfit);
			stmt.setInt(7, game.realDuration);
			stmt.setInt(8, game.informedDuration);
			stmt.setInt(9, game.deliveryDelay);
			stmt.setInt(10, game.unitiesOnTravel);
			stmt.setInt(11, game.initialStock);
			stmt.setBoolean(12, game.informedChainSupply);
			stmt.execute();

			rs = stmt.getGeneratedKeys();
			rs.next();
			game.gameID = rs.getInt(1);

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
	public Game[] getGames()
	{
		Engine engine = new Engine();
		List<Game> games = new ArrayList<>();
		String query;
		ResultSet rs1, rs2;
		Game tmp;
		Node node;

		try
		{
			query = "SELECT * FROM game";
			rs1 = Database.getConnection().prepareStatement(query).executeQuery();

			while(rs1.next())
			{
				tmp = new Game();
				tmp.gameID = rs1.getInt(1);
				tmp.timestamp = rs1.getLong(2);
				tmp.name = rs1.getString(3);
				tmp.password = rs1.getString(4);
				tmp.missingUnitCost = rs1.getDouble(5);
				tmp.stockUnitCost = rs1.getDouble(6);
				tmp.sellingUnitProfit = rs1.getDouble(7);
				tmp.realDuration = rs1.getInt(8);
				tmp.informedDuration = rs1.getInt(9);
				tmp.deliveryDelay = rs1.getInt(10);
				tmp.unitiesOnTravel = rs1.getInt(11);
				tmp.initialStock = rs1.getInt(12);
				tmp.informedChainSupply = rs1.getBoolean(13);
				games.add(tmp);
			}

			for(Game game : games)
			{
				engine.setGame(game, Function.RETAILER);
				engine.buildGame();

				query = "SELECT * FROM demand where game_id = " + game.gameID + " ORDER BY week";
				rs1 = Database.getConnection().prepareStatement(query).executeQuery();

				while(rs1.next())
					game.demand[rs1.getInt(2)] = rs1.getInt(3);

				query = "SELECT * FROM supply_chain where game_id = " + game.gameID + " ORDER BY node_position";
				rs1 = Database.getConnection().prepareStatement(query).executeQuery();

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
			}
		}
		catch(SQLException e)
		{
			System.out.println("Logger::retrieveGameData(Integer gameID): " + e.getSQLState());
			System.out.println("Logger::retrieveGameData(Integer gameID): " + e.getMessage());
		}

		return games.toArray(new Game[0]);
	}
}
