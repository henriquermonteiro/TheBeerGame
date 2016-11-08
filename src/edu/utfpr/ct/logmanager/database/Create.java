package edu.utfpr.ct.logmanager.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/*https://wpollock.com/AJava/DerbyDemo.htm*/
public class Create
{
	private final String game = "CREATE TABLE game( "
								//+ "game_id               INT NOT NULL, "
								+ "game_id               INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), "
								+ "timestamp             BIGINT NOT NULL, "
								+ "name                  VARCHAR(128) NOT NULL, "
								+ "password              VARCHAR(32), "
								+ "missing_unit_cost     DOUBLE NOT NULL CHECK(missing_unit_cost >= 0), "
								+ "stock_unit_cost       DOUBLE NOT NULL CHECK(stock_unit_cost >= 0), "
								+ "selling_unit_profit   DOUBLE NOT NULL CHECK(selling_unit_profit >= 0), "
								+ "real_duration         INT NOT NULL CHECK(real_duration >= 1), "
								+ "informed_duration     INT NOT NULL CHECK(informed_duration >= 1), "
								+ "delivery_delay        INT NOT NULL CHECK(delivery_delay >= 1), "
								+ "unities_on_travel     INT NOT NULL CHECK(unities_on_travel >= 1), "
								+ "initial_stock         INT NOT NULL CHECK(initial_stock >= 1), "
								+ "informed_chain_suplly BOOLEAN NOT NULL DEFAULT FALSE, "
								+ "PRIMARY KEY(game_id) "
								+ ")";

	private final String demand = "CREATE TABLE demand( "
								  + "game_id INT NOT NULL, "
								  + "week    INT NOT NULL CHECK(week >= 0), "
								  + "value   INT NOT NULL CHECK(value >= 0), "
								  + "PRIMARY KEY(game_id, week), "
								  + "FOREIGN KEY(game_id) REFERENCES game(game_id) "
								  + ")";

	private final String supplyChain = "CREATE TABLE supply_chain( "
									   + "game_id       INT NOT NULL, "
									   + "node_position INT NOT NULL CHECK(node_position > 0), "
									   + "player_name   VARCHAR(128) NOT NULL, "
									   + "PRIMARY KEY(game_id, node_position), "
									   + "FOREIGN KEY(game_id) REFERENCES game(game_id) "
									   + ")";

	private final String moves = "CREATE TABLE moves( "
								 + "game_id       INT NOT NULL, "
								 + "node_position INT NOT NULL, "
								 + "week          INT NOT NULL CHECK (week > 0), "
								 + "player_name   VARCHAR(128) NOT NULL, "
								 + "order_placed  INT NOT NULL CHECK (order_placed >= 0), "
								 + "PRIMARY KEY(game_id, node_position, week), "
								 + "FOREIGN KEY(game_id, node_position) REFERENCES supply_chain(game_id, node_position) "
								 + ")";

	public void createTables()
	{
		Connection connection = Database.getConnection();

		createTable(connection, "game", game);
		createTable(connection, "demand", demand);
		createTable(connection, "supply_chain", supplyChain);
		createTable(connection, "moves", moves);
	}

	private void createTable(Connection connection, String tableName, String query)
	{
		Statement stmnt;

		if(!Database.tableExists(tableName))
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
				System.out.println("Create::createTable(Connection connection, String tableName, String query): " + e.getSQLState());
				System.out.println("Create::createTable(Connection connection, String tableName, String query): " + e.getMessage());
			}
		}
	}
}
