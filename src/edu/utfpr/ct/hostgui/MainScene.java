package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IFunction;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.LocalizeHost;
import java.io.File;
import java.util.HashMap;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import edu.utfpr.ct.interfaces.IControllerHost;

public class MainScene extends BorderPane {

    private TabPane tabPane;

    private static final Image homeIcon = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.HOME_ICON)).toURI().toString());
    private static final Image addIcon = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.PLUS_ICON)).toURI().toString());

    private IControllerHost control;
    
    private LoaderPane loaderPane;
    private HashMap<String, GamePane> games;

    public MainScene(IControllerHost control) {
        this.control = control;
        
        games = new HashMap<>();

        loaderPane = new LoaderPane(this);
        
        Tab homeTab = new Tab();

        ImageView iView = new ImageView(homeIcon);
        homeTab.setGraphic(iView);
        homeTab.setClosable(false);
        homeTab.setContent(loaderPane);

        Tab addGameTab = new Tab();

        addGameTab.setClosable(false);
        addGameTab.setGraphic(new ImageView(addIcon));
        addGameTab.setContent(new CreateGamePane(this));

        tabPane = new TabPane(homeTab, addGameTab);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);

        this.setCenter(tabPane);

//        Game g = new Game();
//        g.gameID = 1;
//
//        createGame(g);
//        restoreReport("yak");
    }

    public void makeGameTab(Game game) {
        int state = control.getGameState(game.name);
        
        if(state != -1){
            games.put(game.name, new GamePane(game, control.getGameState(game.name), control.getPlayersOnGame(game.name), this));
        }else{
            games.put(game.name, new GamePane(game, control.getReportState(game.name), null, this));
        }
        Tab gameTab = new Tab(game.name, games.get(game.name));

        this.tabPane.getTabs().add(tabPane.getTabs().size() - 1, gameTab);
    }

    public Game[] gameAvailableGames() {
        return control.getGames();
    }

    public Game[] gameAvailableReports() {
        return control.getReports();
    }

    public void createGame(Game game) {
        if (control.createGame(game)) {
            makeGameTab(control.getGame(game.name));
            loaderPane.update();
        } else {
            // TODO : warn error
        }
    }
    
    public void changePlayerInNode(Game game, String newPlayer, Integer node){
        if(control.changePlayerForNode(game.name, Function.values()[node], newPlayer));
        
        updateGame(game.name);
    }
    
    public void removePlayerFromNode(Game game, Integer node){
        control.removePlayerFromNode(game.name, Function.values()[node]);
    }
    
    public void updateGame(String gameName){
        GamePane p = games.get(gameName);
        
        if(p != null){
            p.updateGame(control.getGame(gameName), control.getGameState(gameName), control.getPlayersOnGame(gameName));
        }
    }

    public void changeGameState(Game game, boolean isStart) {
        if (isStart) {
            control.startGame(game.name);
        } else {
            control.pauseGame(game.name);
        }
    }

    public void changeReportState(Game game, boolean isStart) {
        if (isStart) {
            control.startReport(game.name);
        } else {
            control.pauseReport(game.name);
        }
    }
    
    public void restoreReport(String gameName){
        Game g = control.getReport(gameName);
        
        if(g != null){
            makeGameTab(g);
        }else{
            // TODO warn user
        }
    }
    
    public void restoreGame(String gameName){
        Game g = control.getGame(gameName);
        if(g != null){
            makeGameTab(g);
        }else{
            // TODO warn user
        }
    }
    
    public void purgeGame(String gameName){
        if(control.purgeGame(gameName)){
            loaderPane.update();
        }
    }
    
    public void purgeReport(String gameName){
        if(control.purgeReport(gameName)){
            loaderPane.update();
        }
    }
}
