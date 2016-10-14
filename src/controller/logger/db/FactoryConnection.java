package controller.logger.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class FactoryConnection
{
	private static final String user = "postgres";
	private static final String password = "1234";
	private static final String database = "derby";
	private static final String host = "localhost";
	private static final String port = "5432";

	public static Connection getConnection()
	{
		Connection con = null;
		
		try
		{
			System.out.println("conectado local host");
			String url = "jdbc:derby:DB;create=true";
			con = DriverManager.getConnection(url);
		}
		catch(Exception e)
		{
			System.out.println("local host :( ");
		}
		
		return con;
	}
}
