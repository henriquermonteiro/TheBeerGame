package edu.utfpr.ct.hostgui.utils;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;

public class GameComponent extends GridPane{
    private static final Image deleteIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.DELETE_ICON)).toURI().toString());
    private static final Image reloadIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.LOAD_ICON)).toURI().toString());
    private static final Image viewIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.VIEW_ICON)).toURI().toString());
    
    public static final int LOAD = 1;
    public static final int VIEW = 2;
    
    private Label title;
    private Button loadButton;
    private Button removeButton;

    public GameComponent(String text, int mode) {
        super();
        
        title = new Label(text);
        title.setPadding(new Insets(3));
        Tooltip t = new Tooltip(text);
        title.setTooltip(t);
        
        loadButton = new Button();
        if(mode == LOAD){
            loadButton.setGraphic(new ImageView(reloadIcon));
        }
        
        if(mode == VIEW){
            loadButton.setGraphic(new ImageView(viewIcon));
        }
        
        removeButton = new Button();
        removeButton.setGraphic(new ImageView(deleteIcon));
        
        loadButton.getStyleClass().add("left-pill");
        removeButton.getStyleClass().add("right-pill");
        
        loadButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        removeButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        
        GridPane.setFillWidth(loadButton, Boolean.TRUE);
        GridPane.setFillWidth(removeButton, Boolean.TRUE);
        
        GridPane.setConstraints(title, 0, 0, 2, 1, HPos.CENTER, VPos.CENTER);
        GridPane.setConstraints(loadButton, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
        GridPane.setConstraints(removeButton, 1, 1, 1, 1, HPos.LEFT, VPos.CENTER);
        
        GridPane.setFillWidth(title, Boolean.TRUE);
        
        ColumnConstraints cc = new ColumnConstraints();
        cc.setPercentWidth(50);
        this.getColumnConstraints().addAll(cc, cc);
        
        RowConstraints rc = new RowConstraints();
        rc.setPercentHeight(50);
        this.getRowConstraints().addAll(rc, rc);
        
        
        this.getChildren().addAll(title, loadButton, removeButton);
        
        this.setBorder(
                new Border(
                        new BorderStroke(
                                new Color(0, 0, 0, 0.8), 
                                BorderStrokeStyle.SOLID, 
                                new CornerRadii(5), 
                                new BorderWidths(1.0)
                        )
                )
        );
        this.setPadding(Insets.EMPTY);
        
        this.setBackground(new Background(new BackgroundFill(Color.AZURE, CornerRadii.EMPTY, Insets.EMPTY)));
    }
    
    public void setConfirmButtonAction(EventHandler<ActionEvent> ev){
        loadButton.setOnAction(ev);
    }
    
    public void setDeleteButtonAction(EventHandler<ActionEvent> ev){
        removeButton.setOnAction(ev);
    }
}
