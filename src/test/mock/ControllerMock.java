/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.mock;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.interfaces.ILogger;

/**
 *
 * @author henrique
 */
public class ControllerMock implements IControllerHost{
    private ILogger log;

    public ControllerMock() throws IllegalArgumentException{
        log = new LoggerMock();
    }
    
    @Override
    public boolean createGame(Game game) {
        return true;
    }

    @Override
    public boolean restoreGame(Integer gameID) {
        return true;
    }

    @Override
    public boolean startGame(Integer gameID) {
        return true;
    }

    @Override
    public boolean stopGame(Integer gameID) {
        return true;
    }

    @Override
    public boolean purgeGame(Integer gameID) {
        return true;
    }

    @Override
    public Game getGameReport(String gameName) {
        Game g = log.retrieveGameData(1);
        g.gameID = 3;
        
        return g;
    }

    @Override
    public String[] getAvailableReports() {
        Game[] gs = log.getUnfinishedGamesID();
        String[] ss = new String[gs.length];
        
        int k = 0;
        for(Game g : gs) ss[k++] = g.name;
        
        return ss;
    }

    @Override
    public boolean purgeReport(String gameName) {
        return true;
    }

    @Override
    public boolean changePlayerForNode(Integer gameID, Integer nodeID, String playerName) {
        return true;
    }

    @Override
    public boolean removePlayerFromNode(Integer gameID, Integer nodeID) {
        return true;
    }

    @Override
    public Integer postMoveForNode(Integer gameID, Integer nodeID, Integer move) {
        return move;
    }

    @Override
    public Game getGameRoomData(Integer gameID) {
        return log.retrieveGameData(gameID);
    }

    @Override
    public Game[] getUnfinishedGamesID() {
        return log.getUnfinishedGamesID();
    }

    @Override
    public String[] getPoolForGame(Integer gameID) {
        return new String[]{"Alfonso", "Brás", "Carlos", "Denilson", "Edimar"};
    }

    @Override
    public int getGameState(Integer gameID) {
        return 1 << (gameID % 4);
    }
    
}
