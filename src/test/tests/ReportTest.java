package test.tests;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.gamecontroller.Engine;
import edu.utfpr.ct.report.ReportManager;
import java.util.Date;

public class ReportTest
{
	public void test()
	{
		ReportManager reportManager;
		Comparator comparator;
		Game game;
		Game[] games;

		reportManager = new ReportManager();
		comparator = new Comparator();

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
                game.supplyChain = new AbstractNode[12];
                int k = 0;
                game.supplyChain[k++] = new Node();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new Node();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new Node();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new Node();
                game.supplyChain[k++] = new TravellingTime();
                game.supplyChain[k++] = new TravellingTime();

		Engine engine = new Engine();
		engine.setGame(game, Function.RETAILER);
		engine.buildGame();

		reportManager.createReport(game);
		games = reportManager.getReports();
		//comparator.compareAll(game, games[games.length - 1]);
		//reportManager.purgeReport(game);
	}
}
