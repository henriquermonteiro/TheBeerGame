package edu.utfpr.ct.logmanager.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class Drop
{
	private final String test = "DROP TABLE test";
	private final String game = "DROP TABLE game";
	private final String demand = "DROP TABLE demand";
	private final String demandProfile = "DROP TABLE demand_profile";
	private final String demandGame = "DROP TABLE demand_game";
	private final String supplyChain = "DROP TABLE supply_chain";
	private final String moves = "DROP TABLE moves";
	private final String schema = "DROP SCHEMA APP RESTRICT";

	public void dropTables()
	{
		Connection connection = Util.getConnection();
		
		dropTable(connection, "moves", moves);
		dropTable(connection, "supplyChain", supplyChain);
		dropTable(connection, "demandGame", demandGame);
		dropTable(connection, "demand_profile", demandProfile);
		dropTable(connection, "demand", demand);
		dropTable(connection, "game", game);
		dropTable(connection, "test", test);
		//dropSchema(connection, "DB", schema);
	}

	private void dropTable(Connection connection, String tableName, String query)
	{
		Statement stmnt;

		if(Util.tableExists(connection, tableName))
		{
			System.out.println("Dropping table " + tableName);

			try
			{
				stmnt = connection.createStatement();
				stmnt.execute(query);
				stmnt.close();
			}
			catch(SQLException e)
			{
				System.out.println("Can't drop table: " + e.getSQLState());
			}
		}
	}
	
	private void dropSchema(Connection connection, String schemaName, String query)
	{
		Statement stmnt;

			System.out.println("Dropping schema " + schemaName);

			try
			{
				stmnt = connection.createStatement();
				stmnt.execute(query);
				stmnt.close();
			}
			catch(SQLException e)
			{
				System.out.println("Can't drop schema: " + e.getSQLState());
			}

	}
}
