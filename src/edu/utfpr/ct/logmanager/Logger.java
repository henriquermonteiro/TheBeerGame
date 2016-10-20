package edu.utfpr.ct.logmanager;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.ILogger;
import edu.utfpr.ct.logmanager.db.Util;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Logger implements ILogger
{
	/**
	 * Saves the game session.
	 *
	 * @param game the configured session.
	 */
	public void saveGame(Game game)
	{
		String query;
		PreparedStatement stmt;

		try
		{
			query = "INSERT INTO game(game_id, name, password, missing_unit_cost, stock_unit_cost, selling_unit_profit, real_duration, informed_duration, informed_chain_suplly, delivery_delay) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = Util.getConnection().prepareStatement(query);
			stmt.setInt(1, game.gameID);
			stmt.setString(2, game.name);
			stmt.setString(3, game.password);
			stmt.setDouble(4, game.missingUnitCost);
			stmt.setDouble(5, game.stockUnitCost);
			stmt.setDouble(6, game.sellingUnitProfit);
			stmt.setInt(7, game.realDuration);
			stmt.setInt(8, game.informedDuration);
			stmt.setBoolean(9, game.informedChainSupply);
			stmt.setInt(10, game.deliveryDelay);
			stmt.execute();

			query = "INSERT INTO demand(demand_type, name) "
					+ "VALUES (?, ?)";

			stmt = Util.getConnection().prepareStatement(query);
			stmt.setInt(1, game.demand2.demandType);
			stmt.setString(2, game.demand2.visibleName);
			stmt.execute();

			query = "INSERT INTO demand_game(game_id, demand_type) "
					+ "VALUES (?, ?)";

			stmt = Util.getConnection().prepareStatement(query);
			stmt.setInt(1, game.gameID);
			stmt.setInt(2, game.demand2.demandType);
			stmt.execute();

			query = "INSERT INTO demand_profile(demand_type, week, value) "
					+ "VALUES (?, ?, ?,)";

			for(int i = 0; i < game.demand2.demandProfile.length; i++)
			{
				stmt = Util.getConnection().prepareStatement(query);
				stmt.setInt(1, game.demand2.demandType);
				stmt.setInt(2, i);
				stmt.setInt(3, game.demand2.demandProfile[i]);
				stmt.execute();
			}

			query = "INSERT INTO supply_chain(game_id, node_position, player_name, initial_stock) "
					+ "VALUES (?, ?, ?, ?)";

			for(int i = 0; i < game.supplyChain.size(); i++)
			{
				if(game.supplyChain.get(i) instanceof TravellingTime)
					continue;

				Node node = (Node) game.supplyChain.get(i);
				stmt = Util.getConnection().prepareStatement(query);
				stmt.setInt(1, game.gameID);
				stmt.setInt(2, node.function.getPosition());
				stmt.setString(3, node.playerName);
				stmt.setInt(4, node.initialStock);
				stmt.execute();
			}
		}
		catch(SQLException e)
		{
			System.out.println("Logger::saveGame(Game game): " + e.getSQLState());
			System.out.println("Logger::saveGame(Game game): " + e.getMessage());
		}
	}

	public Game loadGame(int gameID)
	{
		return new LoadGame(gameID).configure();
	}

	/**
	 * Saves the last order placed by the user.
	 *
	 * @param game the game from what the order como from.
	 * @param week the week when the the order was placed.
	 */
	public void updateGame(Game game, int week)
	{
		int nodePosition;
		Node node;
		String query;
		PreparedStatement stmt;

		nodePosition = (game.deliveryDelay + 1) * (week % 4);
		node = (Node) game.supplyChain.get(nodePosition);

		try
		{
			query = "INSERT INTO moves (game_id, node_position, week, player_name, order_placed)"
					+ "VALUES (?, ?, ?, ?, ?)";

			stmt = Util.getConnection().prepareStatement(query);
			stmt.setInt(1, game.gameID);
			stmt.setInt(2, nodePosition);
			stmt.setInt(3, week);
			stmt.setString(4, node.playerName);
			stmt.setInt(5, node.playerMove.get(node.playerMove.size()));
			stmt.execute();
		}
		catch(SQLException e)
		{
			System.out.println("Logger::updateGame(Game game, int week): " + e.getSQLState());
			System.out.println("Logger::updateGame(Game game, int week): " + e.getMessage());
		}
	}

	/**
	 * Deletes a previoulsly saved game.
	 *
	 * @param gameID the ID from the game to be deleted.
	 */
	public void deleteGame(int gameID)
	{
		String query;
		PreparedStatement stmt;

		try
		{
			query = "DELETE FROM moves WHERE game_id = " + gameID;
			stmt = Util.getConnection().prepareStatement(query);
			stmt.execute();
			
			query = "DELETE FROM supply_chain WHERE game_id = " + gameID;
			stmt = Util.getConnection().prepareStatement(query);
			stmt.execute();
			
			query = "DELETE FROM demand WHERE demand_type IN (SELECT demand_type FROM demand WHERE game_id = " + gameID + ")";
			stmt = Util.getConnection().prepareStatement(query);
			stmt.execute();
			
			query = "DELETE FROM demand_profile WHERE demand_type IN (SELECT demand_type FROM demand WHERE game_id = " + gameID + ")";
			stmt = Util.getConnection().prepareStatement(query);
			stmt.execute();
			
			query = "DELETE FROM demand_game WHERE game_id = " + gameID;
			stmt = Util.getConnection().prepareStatement(query);
			stmt.execute();
			
			query = "DELETE FROM game WHERE game_id = " + gameID;
			stmt = Util.getConnection().prepareStatement(query);
			stmt.execute();
		}
		catch(SQLException e)
		{
			System.out.println("Logger::deleteGame(Game game): " + e.getSQLState());
			System.out.println("Logger::deleteGame(Game game): " + e.getMessage());
		}
	}

	public void RetrieveUnfinishedGames()
	{
		throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
	}
}
