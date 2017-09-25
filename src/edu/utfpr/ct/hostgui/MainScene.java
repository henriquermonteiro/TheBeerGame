package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import java.util.HashMap;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.util.IPUtils;
import edu.utfpr.ct.webclient.ActionService;
import java.util.ArrayList;
import java.util.Objects;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class MainScene extends BorderPane {

    private final TabPane tabPane;

    private final IControllerHost control;
    
    private final LoaderPane loaderPane;
    private final HashMap<String, GamePane> games;

    public MainScene(IControllerHost control) {
        this.control = control;
        
        getStyleClass().add("transparent");
        
        games = new HashMap<>();

        loaderPane = new LoaderPane(this);
        
        Tab homeTab = new Tab();

        IconNode home = new IconNode(GoogleMaterialDesignIcons.HOME);
        home.getStyleClass().addAll("icon");
        homeTab.setGraphic(home);
        homeTab.setClosable(false);
        homeTab.setContent(loaderPane);
        homeTab.setTooltip(new Tooltip("Tela inicial"));

        Tab addGameTab = new Tab();

        addGameTab.setClosable(false);
        IconNode add = new IconNode(GoogleMaterialDesignIcons.ADD_CIRCLE_OUTLINE);
        add.getStyleClass().addAll("icon");
        addGameTab.setGraphic(add);
        addGameTab.setContent(new CreateGamePane(this));
        addGameTab.setTooltip(new Tooltip("Criar novo jogo"));

        tabPane = new TabPane(homeTab, addGameTab);
        tabPane.getStyleClass().addAll("main-tabs");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        tabPane.getStyleClass().add("transparent");
        
        ArrayList<String> ips = (ArrayList<String>) IPUtils.findIP();
        
        BorderPane top = new BorderPane(new Label("Endereço para jogadores: " + (ips.isEmpty() ? "Erro ao buscar endereço. Porta usada: " : ips.get(0) +":") + ActionService.getService().getPort()));
        Hyperlink rel = new Hyperlink("reload");
        rel.setVisited(true);
        rel.setOnAction((ActionEvent event) -> {
            top.setCenter(new Label("Endereço para jogadores: " + (ips.isEmpty() ? "Erro ao buscar endereço. Porta usada: " : ips.get(0) +":") + ActionService.getService().getPort()));
        });
        top.setRight(rel);
        top.getStyleClass().addAll("ip-info");
        this.setTop(top);

        this.setCenter(tabPane);
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
        this.tabPane.getSelectionModel().select(gameTab);
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
        if(games.containsKey(gameName)){
            return;
        }
        
        Game g = control.getReport(gameName);
        
        if(g != null){
            makeGameTab(g);
        }else{
            // TODO warn user
        }
    }
    
    public void restoreGame(String gameName){
        if(games.containsKey(gameName)){
            return;
        }
        
        Game g = control.getGame(gameName);
        if(g != null){
            makeGameTab(g);
        }else{
            // TODO warn user
        }
    }
    
    private void closeTab(String tabName){
        int k = 0;
        boolean flag = false;
        
        for(Tab t : tabPane.getTabs()){
            if(Objects.equals(t.getText(), tabName)){
                flag = true;
                break;
            }
            
            k++;
        }
        
        if(flag) tabPane.getTabs().remove(k);
    }
    
    public void purgeGame(String gameName){
        if(control.purgeGame(gameName)){
            if(games.containsKey(gameName)){
                closeTab(gameName);
            }
            
            loaderPane.update();
        }
    }
    
    public void purgeReport(String gameName){
        if(control.purgeReport(gameName)){
            if(games.containsKey(gameName)){
                closeTab(gameName);
            }
            
            loaderPane.update();
        }
    }
}
