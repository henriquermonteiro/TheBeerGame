package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import java.util.HashMap;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.LocalizationUtils;
import edu.utfpr.ct.util.IPUtils;
import edu.utfpr.ct.webserver.ActionService;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Callable;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.web.WebView;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class MainScene extends BorderPane {

    private final TabPane tabPane;
    private final MenuButton langMenuButton;

    private final IControllerHost control;

    private final LoaderPane loaderPane;
    private final HashMap<String, GamePane> games;
    private final StartFrame startFrame;

    public StartFrame getStartFrame() {
        return startFrame;
    }

    public void makeGameInfo(String gameName) {
        try {
            Game g = control.getGame(gameName);
            boolean isGame = true;
            
            if (g == null) {
                g = control.getReport(gameName);
                isGame = false;
            }
            
            if (g == null) {
                return;
            }
            
            Label name = new Label(g.name);
            BorderPane nameInfo = new BorderPane(name);
            nameInfo.getStyleClass().addAll("card", "header", "shadowed-1");
            
            GridPane statusInfo = new GridPane();
            statusInfo.getStyleClass().addAll("card", "top", "shadowed-1", "data");
            
            Text passw = new Text();
            String password = g.password;
            passw.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return (password == null || password.isEmpty() ? HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.INFO_NO_PASSW) : password);
                }
            }, HostLocalizationManager.getInstance().getLang()));
            passw.getStyleClass().addAll("text-node");
            Label passw_Label = new Label();
            LocalizationUtils.bindLocalizationText(passw_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD);
            HBox passw_Layout = new HBox(passw_Label, passw);
            passw_Layout.getStyleClass().add("hbox");
            
            Text status = new Text();
            boolean isGameF = isGame;
            status.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return (isGameF ? HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.INFO_IS_GAME) : HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.INFO_IS_REPORT));
                }
            }, HostLocalizationManager.getInstance().getLang()));
            status.getStyleClass().addAll("text-node");
            Label status_Label = new Label();
            LocalizationUtils.bindLocalizationText(status_Label.textProperty(), HostLocalizationKeys.LABEL_INFO_STATUS);
            HBox status_Layout = new HBox(status_Label, status);
            status_Layout.getStyleClass().add("hbox");
            
            Text informed = new Text(Boolean.toString(g.informedChainSupply));
            boolean isInformed = g.informedChainSupply;
            informed.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
                @Override
                public String call() throws Exception {
                    return (isInformed ? HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.INFO_IS_INFORMED) : HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.INFO_NOT_INFORMED));
                }
            }, HostLocalizationManager.getInstance().getLang()));
            informed.getStyleClass().addAll("text-node");
            Label informed_Label = new Label();
            LocalizationUtils.bindLocalizationText(informed_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INFORMED_SC);
            HBox informed_Layout = new HBox(informed_Label, informed);
            informed_Layout.getStyleClass().add("hbox");
            
            statusInfo.add(passw_Layout, 0, 0, 1, 1);
            statusInfo.add(status_Layout, 1, 0, 1, 1);
            statusInfo.add(informed_Layout, 0, 1, 2, 1);
            
            ColumnConstraints cC = new ColumnConstraints();
            cC.setPercentWidth(50);
            cC.setHalignment(HPos.LEFT);
            cC.setFillWidth(false);
            statusInfo.getColumnConstraints().add(cC);
            statusInfo.getColumnConstraints().add(cC);
            
            GridPane generalInfo = new GridPane();
            generalInfo.getStyleClass().addAll("card", "center", "shadowed-1", "data");
            
            Text missCost = new Text(Double.toString(g.missingUnitCost));
            missCost.getStyleClass().addAll("text-node");
            Label missC_Label = new Label();
            LocalizationUtils.bindLocalizationText(missC_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_MISSINGUC);
            HBox missingCost_Layout = new HBox(missC_Label, missCost);
            missingCost_Layout.getStyleClass().add("hbox");
            
            Text stockCost = new Text(Double.toString(g.stockUnitCost));
            stockCost.getStyleClass().addAll("text-node");
            Label stockCost_Label = new Label();
            LocalizationUtils.bindLocalizationText(stockCost_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_STOCKUC);
            HBox stockCost_Layout = new HBox(stockCost_Label, stockCost);
            stockCost_Layout.getStyleClass().add("hbox");
            
//            Text sellProf = new Text(Double.toString(g.sellingUnitProfit));
//            sellProf.getStyleClass().addAll("text-node");
//            Label sellProf_Label = new Label();
//            LocalizationUtils.bindLocalizationText(sellProf_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_SELLINGP);
//            HBox sellProf_Layout = new HBox(sellProf_Label, sellProf);
//            sellProf_Layout.getStyleClass().add("hbox");
            
            Text realDur = new Text(Double.toString(g.realDuration));
            realDur.getStyleClass().addAll("text-node");
            Label realDur_Label = new Label();
            LocalizationUtils.bindLocalizationText(realDur_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_REAL_DURATION);
            HBox realDur_Layout = new HBox(realDur_Label, realDur);
            realDur_Layout.getStyleClass().add("hbox");
            
            Text infoDur = new Text(Double.toString(g.informedDuration));
            infoDur.getStyleClass().addAll("text-node");
            Label infoDur_Label = new Label();
            LocalizationUtils.bindLocalizationText(infoDur_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INF_DURATION);
            HBox infoDur_Layout = new HBox(infoDur_Label, infoDur);
            infoDur_Layout.getStyleClass().add("hbox");
            
            Text initStock = new Text(Double.toString(g.initialStock));
            initStock.getStyleClass().addAll("text-node");
            Label initStock_Label = new Label();
            LocalizationUtils.bindLocalizationText(initStock_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK);
            HBox initStock_Layout = new HBox(initStock_Label, initStock);
            initStock_Layout.getStyleClass().add("hbox");
            
            Text deliDelay = new Text(Double.toString(g.deliveryDelay));
            deliDelay.getStyleClass().addAll("text-node");
            Label deliDelay_Label = new Label();
            LocalizationUtils.bindLocalizationText(deliDelay_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY);
            HBox deliDelay_Layout = new HBox(deliDelay_Label, deliDelay);
            deliDelay_Layout.getStyleClass().add("hbox");
            
            generalInfo.add(missingCost_Layout, 0, 0, 1, 1);
            generalInfo.add(stockCost_Layout, 0, 1, 1, 1);
//            generalInfo.add(sellProf_Layout, 0, 2, 1, 1);
            
            generalInfo.add(realDur_Layout, 1, 0, 1, 1);
            generalInfo.add(infoDur_Layout, 1, 1, 1, 1);
            
            generalInfo.add(initStock_Layout, 2, 0, 1, 1);
            generalInfo.add(deliDelay_Layout, 2, 1, 1, 1);
            
            generalInfo.setAlignment(Pos.CENTER);
            
            cC = new ColumnConstraints();
            cC.setPercentWidth(100/3);
            cC.setHgrow(Priority.SOMETIMES);
            cC.setHalignment(HPos.LEFT);
            cC.setFillWidth(false);

            generalInfo.getColumnConstraints().addAll(cC, cC, cC);
            
            GridPane chartPane = new GridPane();
            chartPane.getStyleClass().addAll("card", "bottom", "shadowed-1");

            WebView chart = new WebView();
            HostLocalizationManager.getInstance().getLang().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    chart.getEngine().reload();
                }
            });
    
            String url = "http://127.0.0.1:" + ActionService.getService().getPort() + "/info?order=true&game-name=" + URLEncoder.encode(gameName, "UTF-8");
            chart.getEngine().load(url);

            chartPane.add(chart, 0, 0);

            GridPane outterPane = new GridPane();
            outterPane.getStyleClass().addAll("game-info");
            outterPane.add(nameInfo, 0, 0);
            outterPane.add(statusInfo, 0, 1);
            outterPane.add(generalInfo, 0, 2);
            outterPane.add(chartPane, 0, 3);
            outterPane.setAlignment(Pos.CENTER);

            startFrame.makeAlert(outterPane, gameName);

        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
    }

    public MainScene(IControllerHost control, StartFrame startFrame) {
        this.control = control;

        getStyleClass().add("transparent");

        games = new HashMap<>();

        loaderPane = new LoaderPane(this);

        this.startFrame = startFrame;

        Tab homeTab = new Tab();

        IconNode home = new IconNode(GoogleMaterialDesignIcons.HOME);
        home.getStyleClass().addAll("icon");
        homeTab.setGraphic(home);
        homeTab.setClosable(false);
        homeTab.setContent(loaderPane);
        homeTab.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(homeTab.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_MAIN_HOME);

        Tab addGameTab = new Tab();

        addGameTab.setClosable(false);
        IconNode add = new IconNode(GoogleMaterialDesignIcons.ADD_CIRCLE_OUTLINE);
        add.getStyleClass().addAll("icon");
        addGameTab.setGraphic(add);
        addGameTab.setContent(new CreateGamePane(this));
        addGameTab.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(addGameTab.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_MAIN_CREATE);

        tabPane = new TabPane(homeTab, addGameTab);
        tabPane.getStyleClass().addAll("main-tabs", "transparent", TabPane.STYLE_CLASS_FLOATING);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);

        ArrayList<String> ips = (ArrayList<String>) IPUtils.findIP();

        StringProperty ip_begin = new SimpleStringProperty();
        LocalizationUtils.bindLocalizationText(ip_begin, HostLocalizationKeys.TEXT_IP_BEGIN);
        StringProperty ip_middle_error = new SimpleStringProperty();
        LocalizationUtils.bindLocalizationText(ip_middle_error, HostLocalizationKeys.TEXT_IP_MIDDLE_ERROR);
        StringProperty ip_middle = new SimpleStringProperty();
        LocalizationUtils.bindLocalizationText(ip_middle, HostLocalizationKeys.TEXT_IP_MIDDLE);
        StringProperty ip_end = new SimpleStringProperty();
        LocalizationUtils.bindLocalizationText(ip_end, HostLocalizationKeys.TEXT_IP_END);

        Label ip = new Label();
        
        ip.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return ip_begin.get().concat((ips.isEmpty() ? ip_middle_error.get() : ips.get(0))).concat(ip_middle.get().concat("" + ActionService.getService().getPort()).concat(ip_end.get()));
            }
        }, HostLocalizationManager.getInstance().getLang()));
        ip.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(ip.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_COMMON_IP_INFO);
        
        GridPane top = new GridPane();
        IconNode refresh = new IconNode(GoogleMaterialDesignIcons.REFRESH);
        refresh.getStyleClass().addAll("icon");
        Tooltip ipTooltip = new Tooltip();
        Tooltip.install(refresh, ipTooltip);
        LocalizationUtils.bindLocalizationText(ipTooltip.textProperty(), HostLocalizationKeys.TOOLTIP_COMMON_IP);

        Hyperlink rel = new Hyperlink("", refresh);
        rel.setVisited(true);
        rel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<String> ips2 = (ArrayList<String>) IPUtils.findIP();

                Label ip2 = new Label();
                ip2.textProperty().bind(Bindings.createStringBinding(new Callable<String>() {
                    @Override
                    public String call() throws Exception {
                        return ip_begin.get().concat((ips2.isEmpty() ? ip_middle_error.get() : ips2.get(0))).concat(ip_middle.get().concat("" + ActionService.getService().getPort()).concat(ip_end.get()));
                    }
                }, HostLocalizationManager.getInstance().getLang()));

                top.getChildren().remove(top.getChildren().size() - 1);
                top.add(ip2, 1, 0);
            }
        });

        langMenuButton = new MenuButton();
        langMenuButton.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(langMenuButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_COMMON_LANG_BUTTON);
        
        updateLanguageMenuButton();
        makeLanguageMenuItens(langMenuButton);
        top.add(langMenuButton, 0, 0);

        top.add(rel, 2, 0);
        top.add(ip, 1, 0);
        
        ColumnConstraints cCl = new ColumnConstraints();
        cCl.setFillWidth(true);
        cCl.setHalignment(HPos.LEFT);
        cCl.setHgrow(Priority.SOMETIMES);
        cCl.setPercentWidth(25);
        
        ColumnConstraints cCc = new ColumnConstraints();
        cCc.setFillWidth(true);
        cCc.setHalignment(HPos.CENTER);
        cCc.setHgrow(Priority.SOMETIMES);
        cCc.setPercentWidth(50);
        
        ColumnConstraints cCr = new ColumnConstraints();
        cCr.setFillWidth(true);
        cCr.setHalignment(HPos.RIGHT);
        cCr.setHgrow(Priority.SOMETIMES);
        cCr.setPercentWidth(25);
        
        top.getColumnConstraints().addAll(cCl, cCc, cCr);
        
        
        top.getStyleClass().addAll("ip-info", "shadowed-1");
        this.setTop(top);

        this.setCenter(tabPane);
    }

    private void makeLanguageMenuItens(MenuButton menu) {
        menu.getItems().clear();

        String currentLang = HostLocalizationManager.getInstance().getLang().get();

        Image img = null;
        try {
            img = new Image(new FileInputStream(new File(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_FLAG))));
        } catch (FileNotFoundException ex) {
        }
        ImageView flag = new ImageView(img);
        flag.setPreserveRatio(true);
        flag.setFitHeight(30.0);

        Label name = new Label(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_NAME));

        HBox content = new HBox(flag, name);
        content.getStyleClass().addAll("hbox-1");
        MenuItem item = new MenuItem("", content);
        item.getStyleClass().addAll("lang-element", "item", "current");

        menu.getItems().add(item);

        for (String lang : HostLocalizationManager.getValidLanguages()) {
            if (currentLang.equals(lang)) {
                continue;
            }

            try {
                img = new Image(new FileInputStream(new File(HostLocalizationManager.getInstance().getClientFor(lang).getTextFor(HostLocalizationKeys.LANGUAGE_FLAG))));
            } catch (FileNotFoundException ex) {
            }
            flag = new ImageView(img);
            flag.setPreserveRatio(true);
            flag.setFitHeight(30.0);

            Label name2 = new Label(HostLocalizationManager.getInstance().getClientFor(lang).getTextFor(HostLocalizationKeys.LANGUAGE_NAME));

            content = new HBox(flag, name2);
            content.getStyleClass().addAll("hbox-1");
            item = new MenuItem("", content);
            item.getStyleClass().addAll("lang-element", "item");
            item.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    HostLocalizationManager.getInstance().getLang().setValue(name2.getText());

                    makeLanguageMenuItens(langMenuButton);
                    updateLanguageMenuButton();
                }
            });

            menu.getItems().add(item);
        }
    }

    private void updateLanguageMenuButton() {
        langMenuButton.getStyleClass().clear();
        langMenuButton.getStyleClass().addAll("lang-menu");

        HBox content = new HBox();
        content.getStyleClass().addAll("lang-element");

        Image img = null;
        try {
            img = new Image(new FileInputStream(new File(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_FLAG))));
        } catch (FileNotFoundException ex) {
        }
        
        ImageView flag = new ImageView(img);
        flag.setPreserveRatio(true);
        flag.setFitHeight(28.0);

        Label name = new Label(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_NAME));

        IconNode down = new IconNode(GoogleMaterialDesignIcons.EXPAND_MORE);
        down.getStyleClass().add("icon");

        content.getChildren().addAll(flag, name, down);

        langMenuButton.setGraphic(content);
    }

    public void makeGameTab(Game game) {
        int state = control.getGameState(game.name);

        Tab gameTab = new Tab(game.name);
         
        if (state != -1) {
            games.put(game.name, new GamePane(game, control.getGameState(game.name), control.getPlayersOnGame(game.name), this, gameTab));
        } else {
            games.put(game.name, new GamePane(game, control.getReportState(game.name), null, this, gameTab));
        }
        
        gameTab.setContent(games.get(game.name));
        gameTab.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(gameTab.getTooltip().textProperty(), (state != -1 ? HostLocalizationKeys.TOOLTIP_MAIN_GAME : HostLocalizationKeys.TOOLTIP_MAIN_REPO));
        
        gameTab.setOnCloseRequest(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                String name = ((Tab) event.getSource()).getText();

                if (name != null && !name.isEmpty()) {
                    if(games.get(name).isGame()){
                        ((PlayGamePane)games.get(name).getCenter()).playPauseButton.setSelected(false);
                        
                        if(((PlayGamePane)games.get(name).getCenter()).playPauseButton.isSelected()){
                            event.consume();
                            return;
                        }
                    }else{
                        changeReportState(game, false);
                    }
                    
                    games.remove(name);
                }
            }
        });

        this.tabPane.getTabs().add(tabPane.getTabs().size() - 1, gameTab);
        this.tabPane.getSelectionModel().select(gameTab);
    }
    
    public boolean isAnyGameRunning(){
        for(GamePane g : games.values()){
            if(g.isGame()){
                if(((PlayGamePane)g.getCenter()).isGameRunning()) 
                    return true;
            }
        }
        
        return false;
    }

    public Game[] gameAvailableGames() {
        return control.getGames();
    }

    public Game[] gameAvailableReports() {
        return control.getReports();
    }

    public void createGame(Game game) {
        if (control.createGame(game)) {
            changeGameState(game, true);
            makeGameTab(control.getGame(game.name));
            loaderPane.update();
        } else {
            // TODO : warn error
        }
    }

    public void changePlayerInNode(Game game, String newPlayer, Integer node) {
        if (control.changePlayerForNode(game.name, Function.values()[node], newPlayer));

        updateGame(game.name);
    }

    public void removePlayerFromNode(Game game, Integer node) {
        control.removePlayerFromNode(game.name, Function.values()[node]);
    }

    public void updateGame(String gameName) {
        GamePane p = games.get(gameName);

        if (p != null) {
            int state = (p.isGame() ? control.getGameState(gameName) : control.getReportState(gameName));
            Game g = control.getGame(gameName);
            
            if(g == null){
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        loaderPane.update();
                    }
                });
                
                if(state == -1){
                    state = control.getReportState(gameName);
                    g = control.getReport(gameName);
                }else{
                    tabPane.getTabs().remove(p.getTab());
                    return;
                }
            }
            
            p.updateGame(g, state, control.getPlayersOnGame(gameName));
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

    public void restoreReport(String gameName) {
        if (games.containsKey(gameName)) {
            return;
        }

        Game g = control.getReport(gameName);

        if (g != null) {
            changeReportState(g, true);
            makeGameTab(g);
        } else {
            // TODO warn user
        }
    }

    public void restoreGame(String gameName) {
        if (games.containsKey(gameName)) {
            tabPane.getSelectionModel().select(games.get(gameName).getTab());
            return;
        }

        Game g = control.getGame(gameName);
        if (g != null) {
            changeGameState(g, true);
            makeGameTab(g);
        } else {
            // TODO warn user
        }
    }

    public void closeTab(String tabName) {
        int k = 0;
        boolean flag = false;

        for (Tab t : tabPane.getTabs()) {
            if (Objects.equals(t.getText(), tabName)) {
                flag = true;
                break;
            }

            k++;
        }

        if (flag) {
            tabPane.getTabs().remove(k);
            games.remove(tabName);
        }
    }

    public void purgeGame(String gameName) {
        if (control.purgeGame(gameName)) {
            if (games.containsKey(gameName)) {
                closeTab(gameName);
            }

            loaderPane.update();
        }
    }

    public void purgeReport(String gameName) {
        if (control.purgeReport(gameName)) {
            if (games.containsKey(gameName)) {
                closeTab(gameName);
            }

            loaderPane.update();
        }
    }
}
