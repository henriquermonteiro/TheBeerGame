/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.hostgui2.utils.GameComponent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 *
 * @author henrique
 */
public class LoaderPane extends BorderPane {

    private HBox topPane;
    private FlowPane eastPane;
    private FlowPane westPane;

    private MainScene mainScene;

    private void updateAvailable() {
        westPane.getChildren().clear();

        for (Game g : mainScene.gameAvailableGames()) {
            westPane.getChildren().add(new GameComponent(g.name, GameComponent.LOAD));
        }
        
        eastPane.getChildren().clear();
        
        for(String s : mainScene.gameAvailableReports()){
            eastPane.getChildren().add(new GameComponent(s, GameComponent.VIEW));
        }
    }

    public LoaderPane(MainScene mainScene) {
        super();
        this.mainScene = mainScene;

        topPane = new HBox();
        eastPane = new FlowPane(Orientation.HORIZONTAL);
        westPane = new FlowPane(Orientation.HORIZONTAL);
        
        eastPane.setHgap(15);
        eastPane.setVgap(15);
        westPane.setHgap(15);
        westPane.setVgap(15);
        
        eastPane.setAlignment(Pos.CENTER);
        westPane.setAlignment(Pos.CENTER);
        
        eastPane.setPadding(new Insets(15));
        westPane.setPadding(new Insets(15));

        ScrollPane eScroll = new ScrollPane(eastPane);
        ScrollPane wScroll = new ScrollPane(westPane);
        
        eScroll.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                eastPane.setPrefWidth(newValue.doubleValue());
            }
        });
        
        wScroll.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                westPane.setPrefWidth(newValue.doubleValue());
            }
        });

        eScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        wScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        GridPane gridP = new GridPane();

        RowConstraints rC = new RowConstraints();
        rC.setPercentHeight(100);
        rC.setFillHeight(true);
        
        ColumnConstraints cC = new ColumnConstraints();
        cC.setPercentWidth(50);
        cC.setFillWidth(true);

        gridP.getRowConstraints().add(rC);
        gridP.getColumnConstraints().addAll(cC, cC);

        GridPane.setConstraints(wScroll, 0, 0);
        GridPane.setConstraints(eScroll, 1, 0);

        GridPane.setFillWidth(eScroll, Boolean.TRUE);
        GridPane.setFillHeight(eScroll, Boolean.TRUE);
        GridPane.setFillWidth(wScroll, Boolean.TRUE);
        GridPane.setFillHeight(wScroll, Boolean.TRUE);

        gridP.getChildren().addAll(wScroll, eScroll);

//        this.setTop(new ScrollPane(topPane));
        this.setCenter(gridP);
        
        updateAvailable();
    }

}
