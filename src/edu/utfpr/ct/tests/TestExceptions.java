package edu.utfpr.ct.tests;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.gamecontroller.ControllerHost;
import java.util.Date;

public class TestExceptions
{
	public void test()
	{
		ControllerHost ch = new ControllerHost();
		Game game;

		game = new Game();
		game.timestamp = new Date().getTime();
		game.name = "GameTest";
		game.password = "PasswordTest";
		game.missingUnitCost = 1.0;
		game.stockUnitCost = 2.0;
		game.sellingUnitProfit = 3.0;
		game.realDuration = 36;
		game.informedDuration = 50;
		game.deliveryDelay = 2;
		game.unitiesOnTravel = 16;
		game.initialStock = 16;
		game.informedChainSupply = true;

		try
		{
			ch.createGame(game);
			ch.createGame(game);
		}
		catch(IllegalArgumentException e)
		{
			System.out.println("UHUULL: " +  e.getMessage());
		}
	}
}
