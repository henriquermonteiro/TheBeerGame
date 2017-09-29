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
import edu.utfpr.ct.webclient.ActionService;
import java.util.ArrayList;
import java.util.Objects;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class MainScene extends BorderPane {

    private final TabPane tabPane;
    private final MenuButton langMenuButton;
    private MenuItem currentItem;

    private final IControllerHost control;

    private final LoaderPane loaderPane;
    private final HashMap<String, GamePane> games;
    private final StartFrame startFrame;

    public StartFrame getStartFrame() {
        return startFrame;
    }
    
    public void makeGameInfo(String gameName){
        Game g = control.getGame(gameName);
        boolean isGame = true;
        
        if(g == null){
            g = control.getReport(gameName);
            isGame = false;
        }
        
        if(g == null) return;
        
        
        Text name = new Text(g.name);
        BorderPane nameInfo = new BorderPane(name);
        nameInfo.getStyleClass().addAll("card", "shadowed-1");
        
        GridPane statusInfo = new GridPane();
        statusInfo.getStyleClass().addAll("card", "shadowed-1");
        
        Text passw = new Text((g.password == null || g.password.isEmpty() ? "---" : g.password));
        Label passw_Label = new Label();
        LocalizationUtils.bindLocalizationText(passw_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD);
        HBox passw_Layout = new HBox(passw_Label, passw);
        
        Text status = new Text(Boolean.toString(isGame));
        Label status_Label = new Label();
        LocalizationUtils.bindLocalizationText(status_Label.textProperty(), HostLocalizationKeys.LABEL_INFO_STATUS);
        HBox status_Layout = new HBox(status_Label, status);
        
        Text informed = new Text(Boolean.toString(g.informedChainSupply));
        Label informed_Label = new Label();
        LocalizationUtils.bindLocalizationText(informed_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INFORMED_SC);
        HBox informed_Layout = new HBox(informed_Label, informed);
        
        statusInfo.add(passw_Layout, 0, 0, 1, 1);
        statusInfo.add(status_Layout, 1, 0, 1, 1);
        statusInfo.add(informed_Layout, 0, 1, 2, 1);
        
        ColumnConstraints cC = new ColumnConstraints();
        cC.setPercentWidth(50);
        statusInfo.getColumnConstraints().add(cC);
        statusInfo.getColumnConstraints().add(cC);
        
        
        GridPane generalInfo = new GridPane();
        generalInfo.getStyleClass().addAll("card", "shadowed-1");
        
        Text missCost = new Text(Double.toString(g.missingUnitCost));
        Label missC_Label = new Label();
        LocalizationUtils.bindLocalizationText(missC_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_MISSINGUC);
        HBox missingCost_Layout = new HBox(missC_Label, missCost);
        
        Text stockCost = new Text(Double.toString(g.stockUnitCost));
        Label stockCost_Label = new Label();
        LocalizationUtils.bindLocalizationText(stockCost_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_STOCKUC);
        HBox stockCost_Layout = new HBox(stockCost_Label, stockCost);
        
        Text sellProf = new Text(Double.toString(g.sellingUnitProfit));
        Label sellProf_Label = new Label();
        LocalizationUtils.bindLocalizationText(sellProf_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_SELLINGP);
        HBox sellProf_Layout = new HBox(sellProf_Label, sellProf);
        
        Text realDur = new Text(Double.toString(g.realDuration));
        Label realDur_Label = new Label();
        LocalizationUtils.bindLocalizationText(realDur_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_REAL_DURATION);
        HBox realDur_Layout = new HBox(realDur_Label, realDur);
        
        Text infoDur = new Text(Double.toString(g.informedDuration));
        Label infoDur_Label = new Label();
        LocalizationUtils.bindLocalizationText(infoDur_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INF_DURATION);
        HBox infoDur_Layout = new HBox(infoDur_Label, infoDur);
        
        Text initStock = new Text(Double.toString(g.initialStock));
        Label initStock_Label = new Label();
        LocalizationUtils.bindLocalizationText(initStock_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK);
        HBox initStock_Layout = new HBox(initStock_Label, initStock);
        
        Text deliDelay = new Text(Double.toString(g.deliveryDelay));
        Label deliDelay_Label = new Label();
        LocalizationUtils.bindLocalizationText(deliDelay_Label.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY);
        HBox deliDelay_Layout = new HBox(deliDelay_Label, deliDelay);
        
        generalInfo.add(missingCost_Layout, 0, 0, 1, 1);
        generalInfo.add(stockCost_Layout, 1, 0, 2, 1);
        generalInfo.add(sellProf_Layout, 3, 0, 1, 1);
        
        generalInfo.add(realDur_Layout, 0, 1, 2, 1);
        generalInfo.add(infoDur_Layout, 2, 1, 2, 1);
        
        generalInfo.add(initStock_Layout, 0, 2, 2, 1);
        generalInfo.add(deliDelay_Layout, 2, 2, 2, 1);
        
        generalInfo.setAlignment(Pos.CENTER);
        generalInfo.getRowConstraints().add(new RowConstraints(0.0, 10.0, 200.0, Priority.NEVER, VPos.CENTER, false));
        generalInfo.getRowConstraints().add(new RowConstraints(0.0, 10.0, 200.0, Priority.NEVER, VPos.CENTER, false));
        generalInfo.getRowConstraints().add(new RowConstraints(0.0, 10.0, 200.0, Priority.NEVER, VPos.CENTER, false));
        
        generalInfo.getColumnConstraints().add(new ColumnConstraints(40.0, 60.0, 300.0, Priority.SOMETIMES, HPos.CENTER, false));
        generalInfo.getColumnConstraints().add(new ColumnConstraints(40.0, 60.0, 300.0, Priority.SOMETIMES, HPos.CENTER, false));
        generalInfo.getColumnConstraints().add(new ColumnConstraints(40.0, 60.0, 300.0, Priority.SOMETIMES, HPos.CENTER, false));
        generalInfo.getColumnConstraints().add(new ColumnConstraints(40.0, 60.0, 300.0, Priority.SOMETIMES, HPos.CENTER, false));
        
        GridPane outterPane= new GridPane();
        outterPane.add(nameInfo, 0, 0);
        outterPane.add(statusInfo, 0, 1);
        outterPane.add(generalInfo, 0, 2);
        outterPane.setAlignment(Pos.CENTER);
        
        startFrame.makeAlert(outterPane);
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
        tabPane.getStyleClass().addAll("main-tabs");
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        tabPane.getStyleClass().add(TabPane.STYLE_CLASS_FLOATING);
        tabPane.getStyleClass().add("transparent");

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
        ip.textProperty().bind(Bindings.createStringBinding(() -> {
            return ip_begin.get().concat((ips.isEmpty() ? ip_middle_error.get() : ips.get(0))).concat(ip_middle.get().concat("" + ActionService.getService().getPort()).concat(ip_end.get()));
        }, HostLocalizationManager.getInstance().getLang()));
//        BorderPane top = new BorderPane(new Label("Endereço para jogadores: " + (ips.isEmpty() ? "Erro ao buscar endereço. Porta usada: " : ips.get(0) + ":") + ActionService.getService().getPort()));
        BorderPane top = new BorderPane(ip);
        IconNode refresh = new IconNode(GoogleMaterialDesignIcons.REFRESH);
        refresh.getStyleClass().addAll("icon");

        Hyperlink rel = new Hyperlink("", refresh);
        rel.setVisited(true);
        rel.setOnAction((ActionEvent event) -> {
            ArrayList<String> ips2 = (ArrayList<String>) IPUtils.findIP();
            
            Label ip2 = new Label();        
            ip2.textProperty().bind(Bindings.createStringBinding(() -> {
                return ip_begin.get().concat((ips2.isEmpty() ? ip_middle_error.get() : ips2.get(0))).concat(ip_middle.get().concat("" + ActionService.getService().getPort()).concat(ip_end.get()));
            }, HostLocalizationManager.getInstance().getLang()));

            top.setCenter(ip2);
        });

        langMenuButton = new MenuButton();
        updateLanguageMenuButton();
        makeLanguageMenuItens(langMenuButton);
        top.setLeft(langMenuButton);

        top.setRight(rel);
        top.getStyleClass().addAll("ip-info", "shadowed-1");
        this.setTop(top);

        this.setCenter(tabPane);
    }

    private void makeLanguageMenuItens(MenuButton menu) {
        menu.getItems().clear();

        String currentLang = HostLocalizationManager.getInstance().getLang().get();

        String toURL = HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_FLAG);
        Image img = new Image(getClass().getResourceAsStream(toURL));
        ImageView flag = new ImageView(img);
        flag.setPreserveRatio(true);
        flag.setFitHeight(30.0);

        Label name = new Label(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_NAME));

        HBox content = new HBox(flag, name);
        content.getStyleClass().addAll("hbox-1");
        MenuItem item = new MenuItem("", content);
        item.getStyleClass().addAll("lang-element", "item", "current");

        currentItem = item;

        menu.getItems().add(item);

        for (String lang : HostLocalizationManager.getValidLanguages()) {
            if (currentLang.equals(lang)) {
                continue;
            }

            toURL = HostLocalizationManager.getInstance().getClientFor(lang).getTextFor(HostLocalizationKeys.LANGUAGE_FLAG);
            img = new Image(getClass().getResourceAsStream(toURL));
            flag = new ImageView(img);
            flag.setPreserveRatio(true);
            flag.setFitHeight(30.0);

            Label name2 = new Label(HostLocalizationManager.getInstance().getClientFor(lang).getTextFor(HostLocalizationKeys.LANGUAGE_NAME));

            content = new HBox(flag, name2);
            content.getStyleClass().addAll("hbox-1");
            item = new MenuItem("", content);
            item.getStyleClass().addAll("lang-element", "item");
            item.setOnAction((ActionEvent event) -> {
                HostLocalizationManager.getInstance().getLang().setValue(name2.getText());

                makeLanguageMenuItens(langMenuButton);
                updateLanguageMenuButton();
            });

            menu.getItems().add(item);
        }
    }

    private void updateLanguageMenuButton() {
        langMenuButton.getStyleClass().clear();
        langMenuButton.getStyleClass().addAll("lang-menu");

        HBox content = new HBox();
        content.getStyleClass().addAll("lang-element");

        String toURL = HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.LANGUAGE_FLAG);
        Image img = new Image(getClass().getResourceAsStream(toURL));
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

        if (state != -1) {
            games.put(game.name, new GamePane(game, control.getGameState(game.name), control.getPlayersOnGame(game.name), this));
        } else {
            games.put(game.name, new GamePane(game, control.getReportState(game.name), null, this));
        }
        Tab gameTab = new Tab(game.name, games.get(game.name));
        gameTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                String name = ((Tab) event.getSource()).getText();

                if (name != null && !name.isEmpty()) {
                    games.remove(name);
                }
            }
        });

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

    public void restoreReport(String gameName) {
        if (games.containsKey(gameName)) {
            return;
        }

        Game g = control.getReport(gameName);

        if (g != null) {
            makeGameTab(g);
        } else {
            // TODO warn user
        }
    }

    public void restoreGame(String gameName) {
        if (games.containsKey(gameName)) {
            return;
        }

        Game g = control.getGame(gameName);
        if (g != null) {
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
