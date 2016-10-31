package edu.utfpr.ct.logmanager.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Util
{
	private static Connection connection;

	public static Connection getConnection()
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
		catch(Exception e)
		{
			System.out.println("Util::getConnection(): " + e.getMessage());
			return null;
		}
	}

	protected static boolean tableExists(Connection connection, String table)
	{
		int numRows = 0;
		ResultSet rs;

		try
		{
			rs = connection.getMetaData().getTables(null, "APP", table.toUpperCase(), null);
			while(rs.next())
				++numRows;
		}
		catch(SQLException e)
		{
			System.out.println("Util::tableExists(Connection connection, String table): " + e.getMessage());
		}

		return numRows > 0;
	}

	public static void DBInfo()
	{
		ResultSet rs;

		try
		{
			connection = getConnection();
			DatabaseMetaData dbmd = connection.getMetaData();

			System.out.println("Driver name: " + dbmd.getDriverName());
			System.out.println("Driver version: " + dbmd.getDriverVersion());
			System.out.println("Schemas: ");
			
			rs = dbmd.getSchemas();
			while(rs.next())
				System.out.println(rs.getString(1));
		}
		catch(SQLException e)
		{
			System.out.println("Util::DBInfo(): " + e.getMessage());
		}
	}
}
