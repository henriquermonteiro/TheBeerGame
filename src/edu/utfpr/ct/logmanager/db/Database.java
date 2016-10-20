package edu.utfpr.ct.logmanager.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database
{
	public void initializeDB()
	{
		new Create().createTables();
		//new Insert().demand(0, "", null);
		new Drop().dropTables();
	}
	
	public void DBInfo()
	{
		ResultSet rs;

		try
		{
			Connection connection = Util.getConnection();
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
