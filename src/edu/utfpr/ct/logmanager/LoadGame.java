package edu.utfpr.ct.logmanager;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.gamecontroller.IConfiguration;
import edu.utfpr.ct.logmanager.db.Util;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class LoadGame implements IConfiguration
{
	private final int gameID;

	public LoadGame(int gameID)
	{
		this.gameID = gameID;
	}

	@Override
	public Game configure()
	{
		Game game;
		
		game = new Game();
		loadGame(game);
		loadSupplyChain(game);
		loadDemand(game);

		return game;
	}

	private void loadGame(Game game)
	{
		String query;
		PreparedStatement stmt;
		ResultSet rs;

		game = new Game();
		game.supplyChain = new ArrayList<>();

		try
		{
			query = "SELECT * FROM game WHERE gameID = " + gameID;
			stmt = Util.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();

			while(rs.next())
			{
				game.gameID = rs.getInt(1);
				game.name = rs.getString(2);
				game.password = rs.getString(3);
				game.missingUnitCost = rs.getDouble(4);
				game.stockUnitCost = rs.getDouble(5);
				game.sellingUnitProfit = rs.getDouble(6);
				game.realDuration = rs.getInt(7);
				game.informedDuration = rs.getInt(8);
				game.informedChainSupply = rs.getBoolean(9);
				game.deliveryDelay = rs.getInt(10);
			}
		}
		catch(SQLException e)
		{
			System.out.println("LoadGame::loadGame(Game game): " + e.getSQLState());
			System.out.println("LoadGame::loadGame(Game game): " + e.getMessage());
		}
	}

	private void loadSupplyChain(Game game)
	{
		String query;
		PreparedStatement stmt;
		ResultSet rs;
		Node node;
		
		try
		{
			query = "SELECT * FROM supply_chain WHERE gameID = " + gameID;
			stmt = Util.getConnection().prepareStatement(query);
			rs = stmt.executeQuery();
			/*Node node = (Node) game.supplyChain.get(i);
			stmt = Util.getConnection().prepareStatement(query);
			stmt.setInt(1, game.gameID);
			stmt.setInt(2, i);
			stmt.setString(3, node.activePlayerID);
			stmt.setString(4, node.activePlayerID);
			stmt.setInt(5, node.initialStock);
			stmt.execute();*/
			for(int i = 0; rs.next(); i++)
			{
				node = new Node();
				node.playerName = rs.getString(3);
				node.function = null;
				node.initialStock = rs.getInt(4);
//				node.currentStock = rs.getString(3);
//				node.profit = rs.getString(4);
				node.playerMove = new ArrayList<>();
				
				game.supplyChain.add(node);
			}
		}
		catch(SQLException e)
		{
			System.out.println("LoadGame::loadSupplyChain(Game game): " + e.getSQLState());
			System.out.println("LoadGame::loadSupplyChain(Game game): " + e.getMessage());
		}
	}
	
	private void loadPlayerMoves(Game game)
	{
	
	}
	
	private void mountPlayerMoves(Game game)
	{
	
	}

	private void loadDemand(Game game)
	{
		/*Tem que colocar a demanda aqui, tem que ver o que vai ser feito, por perfil, semana a semana, quais as personalizações, etc*/
		//		query = "INSERT INTO demand_game (gameID, demand_type, playerID, playerName, initialStock)"
		//				+ "VALUES (?, ?, ?, ?, ?)";
		//		
		//		for(int i = 0; i < game.supplyChain.size(); i++)
		//		{
		//			stmt = connection.prepareStatement(query);
		//			stmt.setInt(1, game.gameID);
		//			stmt.setInt(2, ?);
		//			stmt.execute();
		//		}
//		catch(SQLException e)
//		{
//			System.out.println("LoadGame::loadGame(Game game): " + e.getSQLState());
//			System.out.println("LoadGame::loadGame(Game game): " + e.getMessage());
//		}
	}
}
