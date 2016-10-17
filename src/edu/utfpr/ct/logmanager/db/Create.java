package edu.utfpr.ct.logmanager.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*https://wpollock.com/AJava/DerbyDemo.htm*/
public class Create
{
	private final String test = "create table test( "
								+ "test int not null"
								+ "primary key (test)"
								+ ");";

	private final String game = "create table game( "
								+ "gameID int not null, "
								+ "name varchar(128) not null, "
								+ "password varchar(32), "
								+ "missingUnitCost float not null check (missingUnitCost >= 0), "
								+ "stockUnitCost float not null check (stockUnitCost >= 0), "
								+ "sellingUnitProfit float not null check(sellingUnitCost >= 0), "
								+ "realDuration int not null check (realDuration >= 1), "
								+ "informedDuration int not null check (informedDuration >= 1), "
								+ "isInformedChainSuplly boolean default false, "
								+ "deliveryDelay int default 2, "
								+ "primary key (gameID)"
								+ ");";

	private final String tableDemand = "create table demand( "
									   + "demand_type int, "
									   + "week int not null check (week >= 0), "
									   + "value int not null check (value >= 0), "
									   + "primary key (demand_type, week) "
									   + ");";

	private final String demandGame = "create table demand_game( "
									  + "gameID int not null, "
									  + "demand_type int not null, "
									  + "primary key (gameID, demand_type), "
									  + "foreign key (gameID) references game(gameID), "
									  + "foreign key (demand_type) references demand(demand_type) "
									  + ");";

	private final String supplyChain = "create table supply_chain( "
									   + "gameID int, "
									   + "nodePosition int check (nodePosition > 0), "
									   + "playerID varchar(128) not null, "
									   + "playerName varchar(128) not null, "
									   + "initialStock int not null check (initialStock >= 0), "
									   + "primary key (gameID, nodePosition), "
									   + "foreign key (gameID) references game(gameID) "
									   + ");";

	private final String moves = "create table moves( "
								 + "gameID int, "
								 + "nodePosition int, "
								 + "week int not null check (week > 0), "
								 + "playerName varchar(128) not null, "
								 + "order_placed int not null check (order_placed >= 0), "
								 + "primary key (gameID, nodePosition, week), "
								 + "foreign key (gameID, nodePosition) references supply_chain(gameID, nodePosition) "
								 + ");";

	public void createTables()
	{
		Connection connection = Util.getConnection();

		createTable(connection, "test", test);
		createTable(connection, "game", game);
		createTable(connection, "tableDemand", tableDemand);
		createTable(connection, "demandGame", demandGame);
		createTable(connection, "supplyChain", supplyChain);
		createTable(connection, "moves", moves);
	}

	private void createTable(Connection connection, String tableName, String query)
	{
		Statement stmnt;

		if(!Util.tableExists(connection, tableName))
		{
			System.out.println("Creating table " + tableName);

			try
			{
				stmnt = connection.createStatement();
				stmnt.execute(query);
				stmnt.close();
			}
			catch(SQLException e)
			{
				System.out.println("Can't create table: " + e.getSQLState());
			}
		}
	}
}
