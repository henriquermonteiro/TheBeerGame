package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.hostgui.utils.GameComponent;
import edu.utfpr.ct.hostgui.utils.ParameterEventHandler;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;

public class LoaderPane extends BorderPane {

    private final FlowPane eastPane;
    private final FlowPane westPane;

    private final MainScene mainScene;

    private void updateAvailable() {
        westPane.getChildren().clear();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        for (Game g : mainScene.gameAvailableGames()) {
            GameComponent gC = new GameComponent(g.name, GameComponent.LOAD, g.name + " criado em " + sdf.format(new Date(g.timestamp)));
            gC.setConfirmButtonAction(new ParameterEventHandler<ActionEvent>(g.name) {

                @Override
                public void handle(ActionEvent event) {
                    if ((params[0] instanceof String)) {
                        mainScene.restoreGame((String) params[0]);
                    }
                }
            });

            gC.setDeleteButtonAction(new ParameterEventHandler<ActionEvent>(g.name) {
                @Override
                public void handle(ActionEvent event) {
                    if ((params[0] instanceof String)) {
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        
                        Localize loc = HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get());
                        String message = loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_GAME_WARN_BEG) + params[0] + loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_GAME_WARN_END);
                        String title = loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_GAME_WARN_TITLE_BEG) + params[0] + loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_GAME_WARN_TITLE_END);
                        confirmation.setContentText(message);
                        confirmation.setHeaderText(title);
                        confirmation.setTitle(title);
                        ((Stage)confirmation.getDialogPane().getScene().getWindow()).getIcons().add(new Image(new File("icon" + File.separator + "Beer_mug_transparent2.png").toURI().toString()));
                        Label icon = new Label();
                        icon.getStyleClass().addAll("warning", "dialog-pane", "alert");
                        confirmation.setGraphic(icon);

                        Optional<ButtonType> res = confirmation.showAndWait();

                        if (res.get() == ButtonType.OK) {
                            mainScene.purgeGame((String) params[0]);
                        }
                    }
                }
            });

            westPane.getChildren().add(gC);
        }

        eastPane.getChildren().clear();

        for (Game g : mainScene.gameAvailableReports()) {
            GameComponent gC = new GameComponent(g.name, GameComponent.VIEW, g.name + " criado em " + sdf.format(new Date(g.timestamp)));
            gC.setConfirmButtonAction(new ParameterEventHandler<ActionEvent>(g.name) {

                @Override
                public void handle(ActionEvent event) {
                    if ((params[0] instanceof String)) {
                        mainScene.restoreReport((String) params[0]);
                    }
                }
            });

            gC.setDeleteButtonAction(new ParameterEventHandler<ActionEvent>(g.name) {
                @Override
                public void handle(ActionEvent event) {
                    if ((params[0] instanceof String)) {
                        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        
                        Localize loc = HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get());
                        String message = loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_REPO_WARN_BEG) + params[0] + loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_REPO_WARN_END);
                        String title = loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_REPO_WARN_TITLE_BEG) + params[0] + loc.getTextFor(HostLocalizationKeys.MESSAGE_DELETE_REPO_WARN_TITLE_END);
                        confirmation.setContentText(message);
                        confirmation.setHeaderText(title);
                        confirmation.setTitle(title);
                        ((Stage)confirmation.getDialogPane().getScene().getWindow()).getIcons().add(new Image(new File("icon" + File.separator + "Beer_mug_transparent2.png").toURI().toString()));
                        Label icon = new Label();
                        icon.getStyleClass().addAll("warning", "dialog-pane", "alert");
                        confirmation.setGraphic(icon);

                        Optional<ButtonType> res = confirmation.showAndWait();

                        if (res.get() == ButtonType.OK) {
                            mainScene.purgeReport((String) params[0]);
                        }
                    }
                }
            });

            eastPane.getChildren().add(gC);
        }
    }

    public LoaderPane(MainScene mainScene) {
        super();
        this.mainScene = mainScene;

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

        getStyleClass().add("transparent");
        eastPane.getStyleClass().add("transparent");
        westPane.getStyleClass().add("transparent");

        ScrollPane eScroll = new ScrollPane(eastPane);
        ScrollPane wScroll = new ScrollPane(westPane);

        eScroll.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            eastPane.setPrefWidth(newValue.doubleValue());
        });

        wScroll.widthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            westPane.setPrefWidth(newValue.doubleValue());
        });

        eScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        wScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        eScroll.getStyleClass().addAll("transparent", "shadowed-1", "right");
        wScroll.getStyleClass().addAll("transparent", "shadowed-1", "left");

        GridPane gridP = new GridPane();
        gridP.getStyleClass().addAll("transparent", "padded-10");

        RowConstraints rC = new RowConstraints();
        rC.setPercentHeight(100);
        rC.setFillHeight(true);

        ColumnConstraints cC = new ColumnConstraints();
        cC.setPercentWidth(50);
        cC.setFillWidth(true);

        gridP.getRowConstraints().add(rC);
        gridP.getColumnConstraints().addAll(cC, cC);
        gridP.setHgap(10);

        GridPane.setConstraints(wScroll, 0, 0);
        GridPane.setConstraints(eScroll, 1, 0);

        GridPane.setFillWidth(eScroll, Boolean.TRUE);
        GridPane.setFillHeight(eScroll, Boolean.TRUE);
        GridPane.setFillWidth(wScroll, Boolean.TRUE);
        GridPane.setFillHeight(wScroll, Boolean.TRUE);

        gridP.getChildren().addAll(wScroll, eScroll);

        this.setCenter(gridP);

        updateAvailable();
    }

    public void update() {
        updateAvailable();
    }

}
