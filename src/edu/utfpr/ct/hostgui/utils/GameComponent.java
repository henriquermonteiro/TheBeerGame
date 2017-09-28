package edu.utfpr.ct.hostgui.utils;

import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.LocalizationUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class GameComponent extends GridPane {

    public static final int LOAD = 1;
    public static final int VIEW = 2;

    private final Label title;
    private final Button loadButton;
    private final Button removeButton;

    public GameComponent(String text, int mode, String tooltip) {
        super();

        getStyleClass().addAll("game-load", "shadowed-1");

        title = new Label(text);
        title.setPadding(new Insets(3));
        title.setTooltip(new Tooltip(tooltip));

        loadButton = new Button();
        removeButton = new Button();
        removeButton.setTooltip(new Tooltip());
        if (mode == LOAD) {
            IconNode reload = new IconNode(GoogleMaterialDesignIcons.REFRESH);
            reload.getStyleClass().add("icon");
            loadButton.setGraphic(reload);

//            loadButton.setTooltip(new Tooltip("Carregar jogo"));
            loadButton.setTooltip(new Tooltip());
            LocalizationUtils.bindLocalizationText(loadButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_GAME_COMP_LOAD_GAME);
            
            LocalizationUtils.bindLocalizationText(removeButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_GAME_COMP_REMO_GAME);
        }

        if (mode == VIEW) {
            IconNode report = new IconNode(GoogleMaterialDesignIcons.LIBRARY_BOOKS);
            report.getStyleClass().add("icon");
            loadButton.setGraphic(report);

//            loadButton.setTooltip(new Tooltip("Carregar relatório"));
            loadButton.setTooltip(new Tooltip());
            LocalizationUtils.bindLocalizationText(loadButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_GAME_COMP_LOAD_REPO);
            
            LocalizationUtils.bindLocalizationText(removeButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_GAME_COMP_REMO_REPO);
        }

        IconNode delete = new IconNode(GoogleMaterialDesignIcons.DELETE_FOREVER);
        delete.getStyleClass().add("icon");

//        removeButton = new Button();
        removeButton.setGraphic(delete);
        
//        removeButton.setTooltip(new Tooltip("Apagar registro"));

        loadButton.getStyleClass().addAll("left", "load");
        removeButton.getStyleClass().addAll("right", "remove");

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

        getChildren().addAll(title, loadButton, removeButton);

        this.setPadding(Insets.EMPTY);

    }

    public void setConfirmButtonAction(EventHandler<ActionEvent> ev) {
        loadButton.setOnAction(ev);
    }

    public void setDeleteButtonAction(EventHandler<ActionEvent> ev) {
        removeButton.setOnAction(ev);
    }
}
