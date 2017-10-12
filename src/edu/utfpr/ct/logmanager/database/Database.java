package edu.utfpr.ct.logmanager.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database
{
	private static Connection connection;

	public void dropDB()
	{
		deleteDir(new File("DB"));
	}

	public void initializeDB()
	{
		new Create().createTables();
	}

	private void deleteDir(File file)
	{
		File[] contents = file.listFiles();

		if(contents != null)
			for(File f : contents)
				deleteDir(f);

		file.delete();
	}

	public static synchronized boolean closeConnection()
	{
		try
		{
			connection.close();
			connection = null;

			return true;
		}
		catch(SQLException e)
		{
			System.out.println("Database::closeConnection(): " + e.getSQLState());
			System.out.println("Database::closeConnection(): " + e.getMessage());

			return false;
		}
	}

	public static synchronized Connection getConnection()
	{
		String connectionString;

		try
		{
			if(connection == null)
			{
				connectionString = "jdbc:derby:DB;create=true";
				connection = DriverManager.getConnection(connectionString);
			}

			return connection;
		}
		catch(SQLException e)
		{
			System.out.println("Database::getConnection(): " + e.getSQLState());
			System.out.println("Database::getConnection(): " + e.getMessage());
			return null;
		}
	}

	public static boolean tableExists(String table)
	{
		int rowsCount = 0;
		ResultSet rs;

		try
		{
			rs = getConnection().getMetaData().getTables(null, "APP", table.toUpperCase(), null);
			if(rs.next())
				rowsCount++;
		}
		catch(SQLException e)
		{
			System.out.println("Database::tableExists(String table): " + e.getSQLState());
			System.out.println("Database::tableExists(String table): " + e.getMessage());
		}

		return rowsCount > 0;
	}

	public static synchronized boolean isConnected()
	{
		return getConnection() != null;
	}

	public void DBInfo()
	{
		DatabaseMetaData dbmd;
		ResultSet rs;

		try
		{
			dbmd = getConnection().getMetaData();

			System.out.println("Driver name: " + dbmd.getDriverName());
			System.out.println("Driver version: " + dbmd.getDriverVersion());
			System.out.println("Schemas: ");

			rs = dbmd.getSchemas();
			while(rs.next())
				System.out.println(rs.getString(1));
		}
		catch(SQLException e)
		{
			System.out.println("Database::DBInfo(): " + e.getSQLState());
			System.out.println("Database::DBInfo(): " + e.getMessage());
		}
	}
}
