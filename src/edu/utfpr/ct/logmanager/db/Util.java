package edu.utfpr.ct.logmanager.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Util
{
	// Derby doesn't support the standard SQL views.  To see if a table
	// exists you normally query the right view and see if any rows are
	// returned (none if no such table, one if table exists).  Derby
	// does support a non-standard set of views which are complicated,
	// but standard JDBC supports a DatabaseMetaData.getTables method.
	// That returns a ResultSet but not one where you can easily count
	// rows by "rs.last(); int numRows = rs.getRow()".  Hence the loop.
	
	public static boolean tableExists(Connection con, String table)
	{
		int numRows = 0;
		
		try
		{
			DatabaseMetaData dbmd = con.getMetaData();
			// Note the args to getTables are case-sensitive!
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
	
	public static Connection getConnection()
	{
		Connection connection;
		String connectionString;
		
		try
		{
			connectionString = "jdbc:derby:DB;create=true";
			connection = DriverManager.getConnection(connectionString);
			
			return connection;
		}
		catch(Exception e)
		{
			System.out.println("public static Connection getConnection(): " + e.getMessage());
			return null;
		}
	}
}
