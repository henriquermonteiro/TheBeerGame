package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.ModelUtils;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.hostgui.utils.AutoCompleteTextField;
import edu.utfpr.ct.hostgui.utils.IdentifiableChangeListener;
import edu.utfpr.ct.hostgui.utils.LockedToggleButton;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.LocalizeHost;
import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

public class PlayGamePane extends BorderPane {

    private static final Image rIcon = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.RETAILER_ICON)).toURI().toString());
    private static final Image wIcon = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.WHOLESALER_ICON)).toURI().toString());
    private static final Image dIcon = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.DISTRIBUTOR_ICON)).toURI().toString());
    private static final Image pIcon = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.PRODUCER_ICON)).toURI().toString());

    private static final Image playIconImage = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.PLAY_ICON)).toURI().toString());
    private static final Image pauseIconImage = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.PAUSE_ICON)).toURI().toString());

    private final Game game;

    private Label gameName;
    private ToggleButton playPauseButton;
    private AutoCompleteTextField[] playersInNodes;
//    private LineChart[] charts;
    private LineChart chart;
//    private XYChart.Series<Integer, Integer>[] chartsData;
    private XYChart.Series<Integer, Integer>[] chartData;
    private ListView<String> pool;
    private Set<String> validPlayers;

    private boolean byPass_setText = false;

    private ImageView playIcon;
    private ImageView pauseIcon;
    private GridPane playerColumns;

    private final MainScene mainScene;

    public void updateGame(Game game, Boolean state, String[] newPool) {
        gameName.setText(game.name);

        validPlayers.clear();

        byPass_setText = true;

        for (int k = 0; k < playersInNodes.length; k++) {
            Node playerNode = ((Node) game.supplyChain[ModelUtils.getActualNodePosition(game, k)]);
            playersInNodes[k].setText(playerNode.playerName);
            playersInNodes[k].setValidText(true);
            if (!playerNode.playerName.isEmpty()) {
                validPlayers.add(playerNode.playerName);
            }
            playersInNodes[k].getEntries().clear();
            playersInNodes[k].getEntries().addAll(Arrays.asList(newPool));

            XYChart.Series xyD = chartData[k];

            if (xyD.getData().size() != playerNode.playerMove.size()) {
                for (int j = xyD.getData().size(); j < playerNode.playerMove.size(); j++) {
                    xyD.getData().add(new XYChart.Data<>(j + 1, playerNode.playerMove.get(j)));
                }
            }

        }

        byPass_setText = false;

        Platform.runLater(() -> {
            playPauseButton.setSelected(state);
            pool.setItems(FXCollections.observableList(Arrays.asList(newPool)));
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
        playIcon = new ImageView(playIconImage);
        pauseIcon = new ImageView(pauseIconImage);
        playIcon.setPreserveRatio(true);
        pauseIcon.setPreserveRatio(true);

        gameName = new Label();

        playPauseButton = new ToggleButton();
        playPauseButton.setGraphic(pauseIcon);

        playPauseButton.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            playPauseButton.setGraphic((newValue ? playIcon : pauseIcon));

            mainScene.changeGameState(game, newValue);
        });

        playersInNodes = new AutoCompleteTextField[game.supplyChain.length / (game.deliveryDelay + 1)];
//        charts = new LineChart[playersInNodes.length];
        chart = new LineChart(new NumberAxis(1.0, game.realDuration, 5.0), new NumberAxis());
        chartData = new XYChart.Series[playersInNodes.length];

        for (int k = 0; k < playersInNodes.length; k++) {
            playersInNodes[k] = new AutoCompleteTextField(k);
//            charts[k] = new LineChart(new NumberAxis(1.0, game.realDuration, 5.0), new NumberAxis());
            chartData[k] = new XYChart.Series<>();

            chart.getData().add(chartData[k]);
        }

        validPlayers = new HashSet<>();

        pool = new ListView<>();
        pool.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        pool.setOnDragDetected((MouseEvent event) -> {
            Dragboard db = pool.startDragAndDrop(TransferMode.MOVE);

            ClipboardContent cbCont = new ClipboardContent();
            cbCont.putString(pool.getSelectionModel().getSelectedItem());
            db.setContent(cbCont);

            event.consume();
        });

        BorderPane topPane = new BorderPane();
        topPane.getStyleClass().addAll("card", "header", "shadowed-1");

        topPane.setCenter(gameName);
        topPane.setRight(playPauseButton);

        pool.setMinWidth(60);

        ScrollPane rightPane = new ScrollPane(pool);
        rightPane.fitToHeightProperty().setValue(Boolean.TRUE);
        rightPane.fitToWidthProperty().setValue(true);

        rightPane.getStyleClass().addAll("card", "right", "shadowed-1");

        ScrollPane centerPane = new ScrollPane();
        centerPane.getStyleClass().addAll("card", "left", "shadowed-1");
        centerPane.fitToHeightProperty().setValue(Boolean.TRUE);

        playerColumns = new GridPane();

        for (int k = 0; k < playersInNodes.length; k++) {
            GridPane gP = new GridPane();

            gP.add(playersInNodes[k], 1, 0);

            LockedToggleButton lTB = new LockedToggleButton();

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

            playersInNodes[k].textProperty().addListener(new IdentifiableChangeListener<String>(playersInNodes[k].getIdentificator()) {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if (byPass_setText) {
                        return;
                    }

                    int test = validPlayerText(newValue);
                    if (test >= -1) {
                        if (test == 0) {
                            playersInNodes[((IdentifiableChangeListener) this).getId()].setValidText(false);
                        }

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

            gP.add(lTB, 2, 0);

            Pane spring = new Pane();
            spring.setMinWidth(30);
            spring.prefWidthProperty().bind(gP.widthProperty().divide(2).subtract(playersInNodes[k].widthProperty().divide(2)));

            gP.add(spring, 0, 0);

            ImageView iV = new ImageView();

            iV.setFitHeight(80);
            iV.setPreserveRatio(true);

            switch (((Node) game.supplyChain[ModelUtils.getActualNodePosition(game, k)]).function.getPosition()) {
                case 1:
                    iV.setImage(rIcon);
                    break;
                case 2:
                    iV.setImage(wIcon);
                    break;
                case 3:
                    iV.setImage(dIcon);
                    break;
                case 4:
                    iV.setImage(pIcon);
                    break;
            }

            Label l = new Label();
            l.setGraphic(iV);

            gP.add(l, 1, 1);
            GridPane.setConstraints(l, 1, 1, 1, 1, HPos.CENTER, VPos.CENTER);
            
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

        playerColumns.add(chart, 0, 1, playersInNodes.length, 1);

        chart.setMinWidth(0);
        chart.setLegendVisible(false);

        if (playersInNodes.length < 6) {
            centerPane.setFitToWidth(true);
        }

        centerPane.setContent(playerColumns);

        GridPane gridP = new GridPane();

        ColumnConstraints cC = new ColumnConstraints();
        cC.setFillWidth(true);
        cC.setHgrow(Priority.ALWAYS);
        cC.setHalignment(HPos.CENTER);

        gridP.getColumnConstraints().add(cC);
        
        cC = new ColumnConstraints();
        cC.setFillWidth(true);
        cC.setHgrow(Priority.SOMETIMES);
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

}
