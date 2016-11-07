/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author henrique
 */
public class MainScene extends BorderPane{
    private TabPane tabPane;
    
    private static final Image homeIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.HOME_ICON)).toURI().toString());
    private static final Image addIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.PLUS_ICON)).toURI().toString());
    
    private IControllerHost control;
    
    public MainScene(IControllerHost control) {
        this.control = control;
        
        Tab homeTab = new Tab();
        
        ImageView iView = new ImageView(homeIcon);
        homeTab.setGraphic(iView);
        homeTab.setClosable(false);
        homeTab.setContent(new LoaderPane(this));
        
        Tab addGameTab = new Tab();
        
        addGameTab.setClosable(false);
        addGameTab.setGraphic(new ImageView(addIcon));
        addGameTab.setContent(new CreateGamePane());
        
        tabPane = new TabPane(homeTab, addGameTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        
        this.setCenter(tabPane);
        
        Game g  = new Game();
        g.gameID = 1;
        
        createGame(g);
    }
    
    public void makeGameTab(Game game){
        Tab gameTab = new Tab(game.name, new PlayGamePane(this, game));
        
        this.tabPane.getTabs().add(tabPane.getTabs().size() - 1, gameTab);
    }
    
    public Game[] gameAvailableGames(){
        return control.getUnfinishedGamesID();
    }
    
    public String[] gameAvailableReports(){
        return control.getAvailableReports();
    }
    
    public void createGame(Game game){
        if(control.createGame(game)){
            makeGameTab(control.getGameRoomData(game.gameID));
        }else{
            // TODO : warn error
        }
    }

    void changeGameState() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
