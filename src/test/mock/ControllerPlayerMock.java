/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.mock;

import edu.utfpr.ct.datamodel.EngineData;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.gamecontroller.Engine;
import edu.utfpr.ct.interfaces.IControllerPlayer;
import edu.utfpr.ct.interfaces.IFunction;

/**
 *
 * @author henrique
 */
public class ControllerPlayerMock implements IControllerPlayer {

    @Override
    public String checkIn(String playerName) {
        return (playerName.equals("juanito") ? "-2" : "");
    }

    @Override
    public Integer postMove(String gameName, String playerName, Integer move) {
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
    public boolean enterGameRoom(String gameName, String playerName, String password) {
        return !password.equals("777");
    }

    @Override
    public boolean selectPlayableNode(String gameName, IFunction function, String playerName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EngineData getGameData(String gameName, String playerName) {
        EngineData engD = new EngineData();
        Game g = new Game();

        g.name = gameName;
        g.gameID = 1;
        
        engD.game = g;
        engD.state = Engine.SETUP;
        engD.weeks = 70;

        return engD;
    }

    @Override
    public int getGameState(String gameName) {
        switch (gameName) {
            case "Turma do barulho":
                return 1;
            case "Turma B300":
                return 8;
            case "Turma S13":
                return 1;
            default:
                return 0;
        }

    }

}
