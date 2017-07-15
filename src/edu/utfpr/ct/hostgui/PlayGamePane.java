package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.ModelUtils;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.hostgui.utils.LockedToggleButton;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import java.util.Arrays;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
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

public class PlayGamePane extends BorderPane {
    private static final Image rIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.RETAILER_ICON)).toURI().toString());
    private static final Image wIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.WHOLESALER_ICON)).toURI().toString());
    private static final Image dIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.DISTRIBUTOR_ICON)).toURI().toString());
    private static final Image pIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.PRODUCER_ICON)).toURI().toString());
    private static final Image playIconImage = new Image(new File(Localize.getTextForKey(LocalizationKeys.PLAY_ICON)).toURI().toString());
    private static final Image pauseIconImage = new Image(new File(Localize.getTextForKey(LocalizationKeys.PAUSE_ICON)).toURI().toString());

    private Game game;

    private Label gameName;
    private ToggleButton playPauseButton;
    private TextField[] playersInNodes;
    private LineChart[] charts;
    private XYChart.Series<Integer, Integer>[] chartsData;
    private ListView<String> pool;

    private ImageView playIcon;
    private ImageView pauseIcon;
    private GridPane playerColumns;

    private MainScene mainScene;

    public void updateGame(Game game, Boolean state, String[] pool) {
        gameName.setText(game.name);

        playPauseButton.setSelected(state);

        for (int k = 0; k < playersInNodes.length; k++) {
            Node playerNode = ((Node) game.supplyChain[ModelUtils.getActualNodePosition(game, k)]);
            playersInNodes[k].setText(playerNode.playerName);

            XYChart.Series xyD = chartsData[k];

            if (xyD.getData().size() != playerNode.playerMove.size()) {
                for (int j = xyD.getData().size(); j < playerNode.playerMove.size(); j++) {
                    xyD.getData().add(new XYChart.Data<>(j + 1, playerNode.playerMove.get(j)));
                }
            }
//            } else {
//
//                XYChart.Data<Number, Number> lastXYD = (XYChart.Data) xyD.getData().get(xyD.getData().size() - 1);
//                if (!lastXYD.getXValue().equals(playerNode.playerMove.size())) {
//                    for (int j = xyD.getData().size(); j < playerNode.playerMove.size(); j++) {
//                        xyD.getData().add(new XYChart.Data<>(j + 1, playerNode.playerMove.get(j)));
//                    }
//                }
//            }

        }

        this.pool.setItems(FXCollections.observableList(Arrays.asList(pool)));

    }
    
    private boolean validPlayerText(String text){
        return pool.getItems().contains(text);
    }

    private void createContent() {
        playIcon = new ImageView(playIconImage);
        pauseIcon = new ImageView(pauseIconImage);
        playIcon.setPreserveRatio(true);
        pauseIcon.setPreserveRatio(true);

        gameName = new Label();

        playPauseButton = new ToggleButton();
        playPauseButton.setGraphic(pauseIcon);

        playPauseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                playPauseButton.setGraphic((newValue ? playIcon : pauseIcon));

                mainScene.changeGameState(game, newValue);
            }
        });

        playersInNodes = new TextField[game.supplyChain.length / (game.deliveryDelay + 1)];
//        playersInNodes = new TextField[game.supplyChain.length];
        charts = new LineChart[playersInNodes.length];
        chartsData = new XYChart.Series[playersInNodes.length];

        for (int k = 0; k < playersInNodes.length; k++) {
            playersInNodes[k] = new TextField();
            charts[k] = new LineChart(new NumberAxis(1.0, game.realDuration, 5.0), new NumberAxis());
            chartsData[k] = new XYChart.Series<>();
            
            charts[k].getData().add(chartsData[k]);
        }

        pool = new ListView<>();
        pool.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        pool.setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Dragboard db = pool.startDragAndDrop(TransferMode.MOVE);
                
                ClipboardContent cbCont = new ClipboardContent();
                cbCont.putString(pool.getSelectionModel().getSelectedItem());
                db.setContent(cbCont);
                
                event.consume();
            }
        });

        BorderPane topPane = new BorderPane();

        topPane.setCenter(gameName);
        topPane.setRight(playPauseButton);

        ScrollPane rightPane = new ScrollPane(pool);
        rightPane.fitToHeightProperty().setValue(Boolean.TRUE);

        ScrollPane centerPane = new ScrollPane();

        playerColumns = new GridPane();

        for (int k = 0; k < playersInNodes.length; k++) {
            GridPane gP = new GridPane();

            gP.add(playersInNodes[k], 1, 0);

            LockedToggleButton lTB = new LockedToggleButton();

            playersInNodes[k].disableProperty().bind(lTB.selectedProperty());
            playersInNodes[k].setOnDragOver(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    if(event.getGestureSource() == pool && event.getDragboard().hasString()){
                        event.acceptTransferModes(TransferMode.ANY);
                    }
                    
                    event.consume();
                }
            });
            
            playersInNodes[k].setOnDragDropped(new EventHandler<DragEvent>() {
                @Override
                public void handle(DragEvent event) {
                    Dragboard db = event.getDragboard();
                    
                    boolean success = false;
                    if(db.hasString()){
                        ((TextField)event.getSource()).setText(db.getString());
                        success = true;
                    }
                    
                    event.setDropCompleted(success);
                    event.consume();
                }
            });
            
            playersInNodes[k].textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    if(validPlayerText(newValue)){
                        mainScene.updateGame(game.name);
                    }
                }
            });

            gP.add(lTB, 2, 0);
            
            Pane spring = new Pane();
            spring.setMinWidth(30);
            spring.prefWidthProperty().bind(gP.widthProperty().divide(2).subtract(playersInNodes[k].widthProperty().divide(2)));
            
            gP.add(spring, 0, 0);

            ImageView iV = new ImageView();
            
            iV.setFitHeight(95);
            iV.setPreserveRatio(true);
            
            switch(((Node)game.supplyChain[ModelUtils.getActualNodePosition(game, k)]).function.getPosition()){
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
            
            playerColumns.add(gP, k, 0);
            
            playerColumns.add(charts[k], k, 1);
            
            charts[k].setMinWidth(0);
            charts[k].setLegendVisible(false);
            
            ColumnConstraints cC = new ColumnConstraints();
            if(playersInNodes.length < 6){
                cC.setPercentWidth(100 / playersInNodes.length);
            }else{
                cC.setMaxWidth(200);
            }
            
            playerColumns.getColumnConstraints().add(cC);
        }
        
        if(playersInNodes.length < 6){
            centerPane.setFitToWidth(true);
//            centerPane.widthProperty().addListener(new ChangeListener<Number>() {
//                @Override
//                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                    playerColumns.setMaxWidth(newValue.doubleValue());
//                }
//            });
        }
        
        centerPane.setContent(playerColumns);

        this.setTop(topPane);
        this.setRight(rightPane);
        this.setCenter(centerPane);
    }

    public PlayGamePane(MainScene mainScene, Game game, Boolean state, String[] pool) {
        super();
        this.game = game;
        this.mainScene = mainScene;

        createContent();

        updateGame(game, state, pool);
    }

}