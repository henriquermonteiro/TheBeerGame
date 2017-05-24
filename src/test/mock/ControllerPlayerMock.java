/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.mock;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IControllerPlayer;

/**
 *
 * @author henrique
 */
public class ControllerPlayerMock implements IControllerPlayer{

    @Override
    public Integer checkIn(String playerName) {
        return (playerName.equals("juanito")? -2 : -1);
    }

    @Override
    public Integer postMove(Integer gameID, Integer nodeID, String playerName, Integer move) {
        return move;
    }

    @Override
    public Game[] listAvailableGameRooms(String playerName) {
        Game g1 = new Game();
        Game g2 = new Game();
        Game g3 = new Game();
        
        g1.gameID = 1;
        g2.gameID = 2;
        g3.gameID = 3;
        
        g1.name = "Turma do barulho";
        g2.name = "Turma B300";
        g3.name = "Turma S13";
        
        g1.password = "true";
        
        return new Game[]{g1, g2, g3};
    }

    @Override
    public boolean enterGameRoom(Integer gameID, String playerName) {
        return true;
    }

    @Override
    public boolean selectPlayableNode(Integer gameID, Integer nodeID, String playerName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Game getGameData(Integer gameID, String playerName) {
        Game g = new Game();
        
        g.name = "Burritos";
        g.gameID = gameID;
        
        
        return g;
    }
    
}
