/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.ModelUtils;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.hostgui2.utils.LockedToggleButton;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import java.util.ArrayList;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

/**
 *
 * @author henrique
 */
public class PlayGamePane extends BorderPane {
    private static final Image rIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.RETAILER_ICON)).toURI().toString());
    private static final Image wIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.WHOLESALER_ICON)).toURI().toString());
    private static final Image dIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.DISTRIBUTOR_ICON)).toURI().toString());
    private static final Image pIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.PRODUCER_ICON)).toURI().toString());

    private Game game;

    private Label gameName;
    private ToggleButton playPauseButton;
    private TextField[] playersInNodes;
    private LineChart[] charts;
    private XYChart.Series<Integer, Integer>[] chartsData;
    private ListView<String> pool;

    private ImageView playIcon;
    private ImageView pauseIcon;

    private MainScene mainScene;

    private void updateGame(Game game) {
        gameName.setText(game.name);

        // TODO update game playPauseButton state
        playPauseButton.setSelected(true);

        for (int k = 0; k < playersInNodes.length; k++) {
            Node playerNode = ((Node) game.supplyChain[ModelUtils.getActualNodePosition(game, k)]);
            playersInNodes[k].setText(playerNode.playerName);

            XYChart.Series xyD = chartsData[k];

            if (xyD.getData().isEmpty() && !playerNode.playerMove.isEmpty()) {
                for (int j = xyD.getData().size(); j < playerNode.playerMove.size(); j++) {
                    xyD.getData().add(new XYChart.Data<>(j + 1, playerNode.playerMove.get(j)));
                }
            } else {

                XYChart.Data<Number, Number> lastXYD = (XYChart.Data) xyD.getData().get(xyD.getData().size() - 1);
                if (!lastXYD.getXValue().equals(playerNode.playerMove.size())) {
                    for (int j = xyD.getData().size(); j < playerNode.playerMove.size(); j++) {
                        xyD.getData().add(new XYChart.Data<>(j + 1, playerNode.playerMove.get(j)));
                    }
                }
            }

        }

        // TODO update game pool list
        pool.setItems(FXCollections.observableList(new ArrayList()));

    }

    private void createContent() {
        playIcon = new ImageView();
        pauseIcon = new ImageView();

        gameName = new Label();

        playPauseButton = new ToggleButton();

        playPauseButton.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                playPauseButton.setGraphic((newValue ? playIcon : pauseIcon));

                // TODO pass parameter
                mainScene.changeGameState();
            }
        });

//        playersInNodes = new TextField[game.supplyChain.length / (game.deliveryDelay + 1)];
        playersInNodes = new TextField[game.supplyChain.length];
        charts = new LineChart[playersInNodes.length];
        chartsData = new XYChart.Series[playersInNodes.length];

        for (int k = 0; k < playersInNodes.length; k++) {
            playersInNodes[k] = new TextField();
            charts[k] = new LineChart(new NumberAxis(), new NumberAxis());
            chartsData[k] = new XYChart.Series<>();
            
            charts[k].getData().add(chartsData[k]);
        }

        pool = new ListView<>();

        BorderPane topPane = new BorderPane();

        topPane.setCenter(gameName);
        topPane.setRight(playPauseButton);

        ScrollPane rightPane = new ScrollPane(pool);
        rightPane.fitToHeightProperty().setValue(Boolean.TRUE);

        ScrollPane centerPane = new ScrollPane();

        GridPane elements = new GridPane();

        for (int k = 0; k < playersInNodes.length; k++) {
            GridPane gP = new GridPane();

            gP.add(playersInNodes[k], 1, 0);

            LockedToggleButton lTB = new LockedToggleButton();

            playersInNodes[k].disableProperty().bind(lTB.selectedProperty());

            gP.add(lTB, 2, 0);

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
            
            elements.add(gP, k, 0);
            elements.add(charts[k], k, 1);
            
            ColumnConstraints cC = new ColumnConstraints();
            if(playersInNodes.length < 6){
                cC.setPercentWidth(100 / playersInNodes.length);
            }else{
                cC.setMaxWidth(200);
            }
            
            elements.getColumnConstraints().add(cC);
        }
        
        centerPane.setContent(elements);

        this.setTop(topPane);
        this.setRight(rightPane);
        this.setCenter(centerPane);
    }

    public PlayGamePane(MainScene mainScene, Game game) {
        super();
        this.game = game;
        this.mainScene = mainScene;

        createContent();

        updateGame(game);
    }

}
