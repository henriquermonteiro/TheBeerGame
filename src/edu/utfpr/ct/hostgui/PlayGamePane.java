package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.ModelUtils;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.hostgui.utils.AutoCompleteTextField;
import edu.utfpr.ct.hostgui.utils.IdentifiableChangeListener;
import edu.utfpr.ct.hostgui.utils.LockedToggleButton;
import edu.utfpr.ct.hostgui.utils.StaticImages;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.LocalizationUtils;
import edu.utfpr.ct.util.ChartJSUtils;
import edu.utfpr.ct.webserver.ActionService;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class PlayGamePane extends BorderPane {

    private final Game game;

    private Label gameName;
    private ToggleButton playPauseButton;
    private AutoCompleteTextField[] playersInNodes;
    private WebView[] charts;
    private ListView<String> pool;
    private Set<String> validPlayers;

    private boolean byPass_setText = false;

    private IconNode playPauseIcon;
    private IconNode tabPlayIcon;
    private GridPane playerColumns;

    private final MainScene mainScene;

    public final void updateGame(Game game, Boolean state, String[] newPool) {
        gameName.setText(game.name);

        validPlayers.clear();

        byPass_setText = true;

        for (int k = 0; k < playersInNodes.length; k++) {
            Node playerNode = ((Node) game.supplyChain[ModelUtils.getActualNodePosition(game, k)]);
            playersInNodes[k].setText(playerNode.playerName);
//            playersInNodes[k].setValidText(true);
            if (!playerNode.playerName.isEmpty()) {
                validPlayers.add(playerNode.playerName);
            }
            playersInNodes[k].getEntries().clear();
            playersInNodes[k].getEntries().addAll(Arrays.asList(newPool));
        }

        byPass_setText = false;

        Platform.runLater(() -> {
            playPauseButton.setSelected(state);
            pool.setItems(FXCollections.observableList(Arrays.asList(newPool)));
            for(WebView wV : charts){
                wV.getEngine().reload();
            }
        });

    }

    private int validPlayerText(String text) {
        for (String s : pool.getItems()) {
            if (s.startsWith(text)) {
                return (s.equals(text) ? 1 : 0);
            }
        }

        for (String s : validPlayers.toArray(new String[0])) {
            if (s.startsWith(text)) {
                return (s.equals(text) ? 2 : -1);
            }
        }

        return (text.isEmpty() ? 0 : -2);
    }

    private void createContent() {
        playPauseIcon = new IconNode(GoogleMaterialDesignIcons.PLAY_ARROW);
        playPauseIcon.getStyleClass().addAll("icon");
        
        tabPlayIcon = new IconNode(GoogleMaterialDesignIcons.PLAY_ARROW);
        tabPlayIcon.getStyleClass().addAll("icon", "small");

        gameName = new Label();

        playPauseButton = new ToggleButton();
        playPauseButton.getStyleClass().addAll("play-pause");
        playPauseButton.setGraphic(playPauseIcon);
        playPauseButton.setTooltip(new Tooltip());
        playPauseButton.getTooltip().textProperty().bind(Bindings.createStringBinding(() -> {
            return (playPauseButton.selectedProperty().get() ? 
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_PLAY_GAME_BUTTON_START) : 
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_PLAY_GAME_BUTTON_PAUSE));
        }, HostLocalizationManager.getInstance().getLang(), playPauseButton.selectedProperty()));

        playPauseButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if(!newValue){
                Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.MESSAGE_STOPGAME_WARN));
                Label icon = new Label();
                icon.getStyleClass().addAll("warning", "dialog-pane", "alert");
                confirm.setGraphic(icon);
                ((Stage)confirm.getDialogPane().getScene().getWindow()).getIcons().add(new Image(new File("icon" + File.separator + "Beer_mug_transparent2.png").toURI().toString()));
                confirm.setHeaderText(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.MESSAGE_STOPGAME_WARN_TITLE));
                confirm.setTitle(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.MESSAGE_STOPGAME_WARN_TITLE));
                Optional<ButtonType> res = confirm.showAndWait();
                
                if (res.get() != ButtonType.OK) {
                    playPauseButton.setSelected(oldValue);
                    return;
                }
            }
            
            mainScene.changeGameState(game, newValue);
            playPauseIcon.setIconCode((newValue ? GoogleMaterialDesignIcons.STOP : GoogleMaterialDesignIcons.PLAY_ARROW));

            if(getParent() instanceof GamePane)
                ((GamePane)getParent()).getTab().setGraphic((newValue ? tabPlayIcon : null));
        });

        playersInNodes = new AutoCompleteTextField[game.supplyChain.length / (game.deliveryDelay + 1)];

        for (int k = 0; k < playersInNodes.length; k++) {
            playersInNodes[k] = new AutoCompleteTextField(k);
        }

        validPlayers = new HashSet<>();

        pool = new ListView<>();
        pool.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        pool.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(pool.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_PLAY_GAME_PL_POOL);

        pool.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = pool.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent cbCont = new ClipboardContent();
            cbCont.putString(pool.getSelectionModel().getSelectedItem());
            db.setContent(cbCont);

            event.consume();
        });

        BorderPane topPane = new BorderPane();
        topPane.getStyleClass().addAll("card", "header", "shadowed-1");
        
        Hyperlink info = new Hyperlink();
        IconNode infoIcon = new IconNode(GoogleMaterialDesignIcons.INFO_OUTLINE);
        infoIcon.getStyleClass().addAll("icon");
        
        Tooltip infoTooltip = new Tooltip();
        LocalizationUtils.bindLocalizationText(infoTooltip.textProperty(), HostLocalizationKeys.TOOLTIP_PLAY_GAME_INFO);
        Tooltip.install(info, infoTooltip);
        
        info.setGraphic(infoIcon);
        info.setOnAction((event) -> {
            mainScene.makeGameInfo(game.name);
        });

        topPane.setCenter(gameName);
        topPane.setRight(playPauseButton);
        topPane.setLeft(info);

        pool.setMinWidth(120);

        ScrollPane rightPane = new ScrollPane(pool);
        rightPane.fitToHeightProperty().setValue(Boolean.TRUE);
        rightPane.fitToWidthProperty().setValue(true);
        rightPane.setMinWidth(150);

        rightPane.getStyleClass().addAll("card", "right", "shadowed-1");

        GridPane centerPane = new GridPane();
        centerPane.getStyleClass().addAll("card", "left", "shadowed-1");

        playerColumns = new GridPane();

        for (int k = 0; k < playersInNodes.length; k++) {
            GridPane gP = new GridPane();
            
            Label name = new Label();
            LocalizationUtils.bindLocalizationText(name.textProperty(), HostLocalizationKeys.CHART_OR_FUNCTION_AX+(k+1));
            
            gP.add(name, 0, 0, 3, 1);

            gP.add(playersInNodes[k], 1, 1);

            LockedToggleButton lTB = new LockedToggleButton();
            lTB.setTooltip(new Tooltip());
            int function = k+1;
            lTB.getTooltip().textProperty().bind(Bindings.createStringBinding(() -> {
                return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_PLAY_GAME_LOCKUN_BEGIN).concat(
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.CHART_OR_FUNCTION_AX+function)).concat(
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_PLAY_GAME_LOCKUN_END));
            }, HostLocalizationManager.getInstance().getLang()));

            playersInNodes[k].disableProperty().bind(lTB.selectedProperty());
            playersInNodes[k].setOnDragOver((DragEvent event) -> {
                if (event.getGestureSource() == pool && event.getDragboard().hasString()) {
                    event.acceptTransferModes(TransferMode.ANY);
                }

                event.consume();
            });

            playersInNodes[k].setOnDragDropped((DragEvent event) -> {
                Dragboard db = event.getDragboard();

                boolean success = false;
                if (db.hasString()) {
                    ((TextField) event.getSource()).setText(db.getString());
                    success = true;
                }

                event.setDropCompleted(success);
                event.consume();
            });
            
            int ref = k;
            playersInNodes[k].focusedProperty().addListener((observable) -> {
                if (byPass_setText) {
                    return;
                }

                int test = validPlayerText(playersInNodes[ref].getText());
                if (test <= 0) {
                    playersInNodes[ref].setText("");
                }
             });

            playersInNodes[k].textProperty().addListener(new IdentifiableChangeListener<String>(playersInNodes[k].getIdentificator()) {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (byPass_setText) {
                        return;
                    }

                    int test = validPlayerText(newValue);
                    if (test >= -1) {
                        if (test == 1) {
                            mainScene.changePlayerInNode(game, newValue, ((IdentifiableChangeListener) this).getId());
                            mainScene.updateGame(game.name);
                        }

                        if (test == -1) {
                            mainScene.removePlayerFromNode(game, ((IdentifiableChangeListener) this).getId());
                            Platform.runLater(() -> {
                                mainScene.updateGame(game.name);
                            });
                        }
                    } else {
                        playersInNodes[((IdentifiableChangeListener) this).getId()].setText(oldValue);
                    }
                }
            });
            
            playersInNodes[k].setTooltip(new Tooltip());
            playersInNodes[k].getTooltip().textProperty().bind(Bindings.createStringBinding(() -> {
                return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_PLAY_GAME_POSIT_BEGIN).concat(
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.CHART_OR_FUNCTION_AX+function)).concat(
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_PLAY_GAME_POSIT_END));
            }, HostLocalizationManager.getInstance().getLang()));

            gP.add(lTB, 2, 1);

            ImageView iV = new ImageView();

            iV.setFitHeight(65);
            iV.setPreserveRatio(true);
            
            ColorAdjust bright = new ColorAdjust();
            bright.setBrightness(0.3);
            bright.setContrast(0.1);
            
            Lighting light = new Lighting();
            light.setDiffuseConstant(1.0);
            light.setSpecularConstant(0.0);
            light.setSpecularExponent(0.0);
            light.setSurfaceScale(0.0);
            
            bright.setInput(light);

            switch (((Node) game.supplyChain[ModelUtils.getActualNodePosition(game, k)]).function.getPosition()) {
                case 1:
                    iV.setImage(StaticImages.RETAILER_ICON);
                    light.setLight(new Light.Distant(45, 45, Color.web(ChartJSUtils.COLORS_S[1])));
                    break;
                case 2:
                    iV.setImage(StaticImages.WHOLESALER_ICON);
                    light.setLight(new Light.Distant(45, 45, Color.web(ChartJSUtils.COLORS_S[2])));
                    break;
                case 3:
                    iV.setImage(StaticImages.DISTRIBUTOR_ICON);
                    light.setLight(new Light.Distant(45, 45, Color.web(ChartJSUtils.COLORS_S[3])));
                    break;
                case 4:
                    iV.setImage(StaticImages.PRODUCER_ICON);
                    light.setLight(new Light.Distant(45, 45, Color.web(ChartJSUtils.COLORS_S[4])));
                    break;
            }
            
            iV.setEffect(bright);

            Label l = new Label();
            l.setGraphic(iV);

            gP.add(l, 1, 2);
            
            ColumnConstraints cC1 = new ColumnConstraints();
            cC1.setFillWidth(true);
            cC1.setHalignment(HPos.CENTER);
            cC1.setHgrow(Priority.SOMETIMES);
            cC1.setPercentWidth(20);
            ColumnConstraints cC2 = new ColumnConstraints();
            cC2.setHalignment(HPos.CENTER);
            cC2.setFillWidth(true);
            cC2.setHgrow(Priority.SOMETIMES);
            cC2.setPercentWidth(60);
            ColumnConstraints cC3 = new ColumnConstraints();
            cC3.setHalignment(HPos.CENTER);
            cC3.setFillWidth(true);
            cC3.setHgrow(Priority.SOMETIMES);
            cC3.setPercentWidth(20);
            
            gP.getColumnConstraints().addAll(cC1, cC2, cC3);
            
            gP.setVgap(3.0);
            gP.setHgap(3.0);
            gP.setPadding(new Insets(5.0));

            playerColumns.add(gP, k, 0);

            ColumnConstraints cC = new ColumnConstraints();
            if (playersInNodes.length < 6) {
                cC.setPercentWidth(100 / playersInNodes.length);
            } else {
                cC.setMaxWidth(200);
            }

            playerColumns.getColumnConstraints().add(cC);
        }
        
        RowConstraints rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.SOMETIMES);
        rC.setValignment(VPos.CENTER);
        
        playerColumns.getRowConstraints().add(rC);
        
        rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.ALWAYS);
        rC.setValignment(VPos.TOP);
        
        playerColumns.getRowConstraints().add(rC);

        centerPane.add(playerColumns, 0, 0);
        
        TabPane charts_tab = new TabPane();
        charts_tab.getStyleClass().addAll("main-tabs", "transparent", TabPane.STYLE_CLASS_FLOATING);
        
        charts = new WebView[2];
        
        Tab orders = new Tab();
        orders.setClosable(false);
        LocalizationUtils.bindLocalizationText(orders.textProperty(), HostLocalizationKeys.CHART_OR_TITLE);
        
        charts[0] = new WebView();
        try {
            charts[0].getEngine().load("http://127.0.0.1:" + ActionService.getService().getPort() + "/info?order=true&no-legend=true&game-name=" + URLEncoder.encode(game.name, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        
        HostLocalizationManager.getInstance().getLang().addListener((observable) -> {
            charts[0].getEngine().reload();
        });
        
        orders.setContent(charts[0]);
        
        Tab stock = new Tab();
        stock.setClosable(false);
        LocalizationUtils.bindLocalizationText(stock.textProperty(), HostLocalizationKeys.CHART_ST_TITLE);
        
        charts[1] = new WebView();
        try {
            charts[1].getEngine().load("http://127.0.0.1:" + ActionService.getService().getPort() + "/info?stock=true&no-legend=true&game-name=" + URLEncoder.encode(game.name, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        
        HostLocalizationManager.getInstance().getLang().addListener((observable) -> {
            charts[1].getEngine().reload();
        });
        
        stock.setContent(charts[1]);
        
        charts_tab.getTabs().addAll(orders, stock);
        
        BorderPane chartP = new BorderPane(charts_tab);
        chartP.setPadding(new Insets(5));
        
        centerPane.add(chartP, 0, 1);
        
        ColumnConstraints cC = new ColumnConstraints();
        cC.setFillWidth(true);
        cC.setHgrow(Priority.ALWAYS);
        cC.setHalignment(HPos.CENTER);
        
        centerPane.getColumnConstraints().add(cC);
        
        rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.SOMETIMES);
        
        centerPane.getRowConstraints().add(rC);
        
        rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.ALWAYS);
        
        centerPane.getRowConstraints().add(rC);

        GridPane gridP = new GridPane();

        cC = new ColumnConstraints();
        cC.setFillWidth(true);
        cC.setHgrow(Priority.ALWAYS);
        cC.setHalignment(HPos.CENTER);

        gridP.getColumnConstraints().add(cC);
        
        cC = new ColumnConstraints();
        cC.setFillWidth(true);
        cC.setHgrow(Priority.ALWAYS);
        cC.setHalignment(HPos.CENTER);
        
        gridP.getColumnConstraints().add(cC);
        
        rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.SOMETIMES);
        rC.setValignment(VPos.CENTER);
        
        gridP.getRowConstraints().add(rC);
        
        rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.ALWAYS);
        rC.setValignment(VPos.CENTER);
        
        gridP.getRowConstraints().addAll(rC);
        
        gridP.add(topPane, 0, 0, 2, 1);
        gridP.add(centerPane, 0, 1, 1, 1);
        gridP.add(rightPane, 1, 1, 1, 1);
        
        gridP.setVgap(20.0);
        gridP.setHgap(10.0);
        
        this.setCenter(gridP);
        BorderPane.setMargin(gridP, new Insets(10.0, 10.0, 10.0, 10.0));
    }

    public PlayGamePane(MainScene mainScene, Game game, Boolean state, String[] pool) {
        super();
        this.game = game;
        this.mainScene = mainScene;

        createContent();

        updateGame(game, state, pool);
    }

    public boolean isGameRunning(){
        return playPauseButton.isSelected();
    }
}
