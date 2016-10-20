package edu.utfpr.ct.logmanager.db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Insert
{
	private final String demand = "INSERT INTO demand"
								  + "(demand_type, name) "
								  + "VALUES (?, ?)";

	private final String demandProfile = "INSERT INTO demand_profile"
										 + "(demand_type, week, value) "
										 + "VALUES (?, ?, ?)";

	protected void demand(int demandType, String name, int[] value)
	{
		PreparedStatement stmt;

		try
		{
			stmt = Util.getConnection().prepareStatement(demand);
			stmt.setInt(1, demandType);
			stmt.setString(2, name);
			stmt.execute();

			for(int i = 0; i < value.length; i++)
			{
				stmt = Util.getConnection().prepareStatement(demandProfile);
				stmt.setInt(1, demandType);
				stmt.setInt(2, i);
				stmt.setInt(3, value[i]);
				stmt.execute();
			}
		}
		catch(SQLException e)
		{
			System.out.println("Logger::db::demand(int demandType, String name, int[] value): " + e.getSQLState());
			System.out.println("Logger::db::demand(int demandType, String name, int[] value): " + e.getMessage());
		}
	}
}
