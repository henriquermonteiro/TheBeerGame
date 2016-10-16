package edu.utfpr.ct.gamecontroller;

import edu.utfpr.ct.logmanager.db.FactoryConnection;

public class TheBeerGame
{
	public static void main(String[] args)
	{
		FactoryConnection.getConnection();
	}
}
