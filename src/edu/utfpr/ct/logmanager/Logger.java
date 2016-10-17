package edu.utfpr.ct.logmanager;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.ILogger;
import edu.utfpr.ct.logmanager.db.Util;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Logger implements ILogger
{
	public void saveGame(Game game)
	{
		String query;
		Connection connection;
		PreparedStatement stmt;

		connection = Util.getConnection();

		try
		{
			query = "INSERT INTO game (gameID, name, password, missingUnitCost, stockUnitCost, sellingUnitProfit, realDuration, informedDuration, isInformedChainSuplly, deliveryDelay) "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

			stmt = connection.prepareStatement(query);
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

			query = "INSERT INTO supply_chain (gameID, nodePosition, playerID, playerName, initialStock)"
					+ "VALUES (?, ?, ?, ?, ?)";

			for(int i = 0; i < game.supplyChain.size(); i++)
			{
				if(game.supplyChain.get(i) instanceof TravellingTime)
					continue;

				Node node = (Node) game.supplyChain.get(i);
				stmt = connection.prepareStatement(query);
				stmt.setInt(1, game.gameID);
				stmt.setInt(2, i);
				stmt.setString(3, node.activePlayerID);
				stmt.setString(4, node.activePlayerID);
				stmt.setInt(5, node.initialStock);
				stmt.execute();
			}
			
			
			/*Tem que colocar a demanda aqui, tem que ver o que vai ser feito, por perfil, semana a semana, quais as personalizações, etc*/
//			query = "INSERT INTO demand_game (gameID, demand_type, playerID, playerName, initialStock)"
//					+ "VALUES (?, ?, ?, ?, ?)";
//			
//			for(int i = 0; i < game.supplyChain.size(); i++)
//			{
//				stmt = connection.prepareStatement(query);
//				stmt.setInt(1, game.gameID);
//				stmt.setInt(2, ?);
//				stmt.execute();
//			}
		}
		catch(SQLException ex)
		{
			String theError = ex.getSQLState();
			System.out.println("public void saveGame(Game game): " + theError);
		}
	}

	public Game loadGame()
	{
		return null;
	}

	public void updateGame(Game game)
	{
//		for(int k : ((Node) abstractNode).playerMove)
//		{
//			query = "INSERT INTO moves (gameID, nodePosition, week, playerName, order_placed)"
//					+ "VALUES (?, ?, ?, ?, ?)";
//
//			stmt = connection.prepareStatement(query);
//			stmt.setInt(1, game.gameID);
//			stmt.setInt(2, i);
//			stmt.setString(3, ((Node) abstractNode).activePlayerID);
//			stmt.setString(4, ((Node) abstractNode).activePlayerID);
//			stmt.setInt(5, ((Node) abstractNode).initialStock);
//			stmt.execute();
//		}
	}

	public void deleteGame(Game game)
	{

	}
}
