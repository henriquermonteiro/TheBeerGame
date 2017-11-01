package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Game;
import javafx.application.Platform;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

public class GamePane extends BorderPane {

    public final Game game;
    private final MainScene mainScene;
    private Boolean isGame;
    private BorderPane center;
    private final Tab tab;
    private final GamePane thisGamePane;

    public GamePane(Game game, Integer state, String[] pool, MainScene mainScene, Tab tab) {
        super();
        this.game = game;
        this.tab = tab;
        this.mainScene = mainScene;

        isGame = (state <= 4);
        
        thisGamePane = this;

        if (isGame) {
            center = new PlayGamePane(mainScene, game, state <= 2, pool);
            this.setCenter(center);
        } else {
            center = new ReportGamePane(game, state == 8, mainScene);
            this.setCenter(center);
        }
    }

    public void updateGame(Game game, Integer state, String[] pool) {
        if (state <= 4) {
            ((PlayGamePane) center).updateGame(game, state <= 2, pool);

        } else if (isGame) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    center = new ReportGamePane(game, state == 8, mainScene);
                    thisGamePane.setCenter(center);
                    isGame = false;
                }
            });
        } else {
            ((ReportGamePane) center).updateReport(game, state == 8);
        }
    }

    public Tab getTab() {
        return tab;
    }

    public Boolean isGame() {
        return isGame;
    }
    
}
