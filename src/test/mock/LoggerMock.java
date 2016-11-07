/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.mock;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.datamodel.TravellingTime;
import edu.utfpr.ct.interfaces.ILogger;
import java.util.LinkedList;

/**
 *
 * @author henrique
 */
public class LoggerMock implements ILogger{

    @Override
    public void logGameStart(Game game) {
        System.out.println("Beep, boop, poft! \n Jogo salvo.");
    }

    @Override
    public void logPlayerMove(Integer gameID, Integer nodeID, String playerName, Integer week, Integer move) {
        System.out.println("Beeep! \n Ação salva.");
    }

    @Override
    public void purgeGame(Integer gameID) {
        System.out.println("Krrrrrrr ... Poft ... \n Jogo apagado.");
    }

    @Override
    public Game[] getUnfinishedGamesID() {
        Game[] ids = new Game[3];
        
        Game g = new Game();
        g.gameID = 1;
        g.name = "Turma s11 - 2016/2";
        
        ids[0] = g;
        
        g = new Game();
        g.gameID = 2;
        g.name = "Turma s13 - 2016/2";
        
        ids[1] = g;
        
        g = new Game();
        g.gameID = 3;
        g.name = "Turma s73 - 2015/2";
        
        ids[2] = g;
        
        return ids;
    }

    @Override
    public Game retrieveGameData(Integer gameID) {
        Game g = new Game();
        g.gameID = gameID;
        g.name = "Teste";
        g.password = "";
        g.informedChainSupply = false;
        g.deliveryDelay = 2;
        g.informedDuration = 70;
        g.realDuration = 40;
        g.missingUnitCost = 2;
        g.sellingUnitProfit = 0;
        g.stockUnitCost = 1;
        g.demand = new int[g.realDuration];
        g.unitiesOnTravel = 0;
        
        for(int k = 0; k < g.demand.length; k++) g.demand[k] = (k < 10 ? 5 : 10);
        
        g.supplyChain = new AbstractNode[4];
        
        Node n = new Node();
        n.travellingStock = 0;
        n.profit = 5.0 + 0.0;
        n.playerName = "Zezinho";
//        n.initialStock = 10;
        n.function = Function.RETAILER;
        n.currentStock = 0;
        n.playerMove = new LinkedList<>();
        
        n.playerMove.add(5);
        n.playerMove.add(5);
        
        g.supplyChain[0] = n;
        
        n = new Node();
        n.travellingStock = 0;
        n.profit = 5.0 + 0.0;
        n.playerName = "Pedrinho";
//        n.initialStock = 10;
        n.function = Function.WHOLESALER;
        n.currentStock = 0;
        n.playerMove = new LinkedList<>();
        
        n.playerMove.add(5);
        n.playerMove.add(5);
        
        g.supplyChain[1] = n;
        
        n = new Node();
        n.travellingStock = 0;
        n.profit = 5.0 + 0.0;
        n.playerName = "Luizinho";
//        n.initialStock = 10;
        n.function = Function.DISTRIBUTOR;
        n.currentStock = 0;
        n.playerMove = new LinkedList<>();
        
        n.playerMove.add(5);
        n.playerMove.add(5);
        
        g.supplyChain[2] = n;
        
        n = new Node();
        n.travellingStock = 0;
        n.profit = 5.0 + 0.0;
        n.playerName = "Jorjão";
//        n.initialStock = 10;
        n.function = Function.PRODUCER;
        n.currentStock = 0;
        n.playerMove = new LinkedList<>();
        
        n.playerMove.add(10);
        n.playerMove.add(5);
        
        g.supplyChain[3] = n;
        
        return g;
    }

    @Override
    public void logPlayerMove(int gameID, Node node) {
        System.out.println("Beeep! \n Ação salva.");
    }

	@Override
	public Game[] getUnfinishedGames()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}
    
}
