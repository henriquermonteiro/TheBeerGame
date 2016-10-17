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
			System.out.println("public static Connection getConnection(): " + e.getMessage());
			return null;
		}
	}
	
	protected static boolean tableExists(Connection con, String table)
	{
		int numRows = 0;
		
		try
		{
			DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, "APP", table.toUpperCase(), null);
			while(rs.next())
				++numRows;
		}
		catch(SQLException e)
		{
			String theError = e.getSQLState();
			System.out.println("Can't query DB metadata: " + theError);
			System.exit(1);
		}
		
		return numRows > 0;
	}
}
