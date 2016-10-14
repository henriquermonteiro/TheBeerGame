package controller;

import controller.logger.db.FactoryConnection;


public class TheBeerGame
{
	public static void main(String[] args)
	{
		FactoryConnection.getConnection();
	}
}
