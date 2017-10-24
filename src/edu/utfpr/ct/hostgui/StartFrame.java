package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.gamecontroller.Controller;
import edu.utfpr.ct.hostgui.utils.BubblePanel;
import edu.utfpr.ct.interfaces.IGUI;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.LocalizationUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconFontFX;

public class StartFrame extends Application implements IGUI{
    private MainScene mainScene;
    private final IControllerHost controller;
    private Stage primaryStage;
    
    private final ArrayList<Stage> infos;

    public StartFrame() {
        controller = Controller.getController();
        infos = new ArrayList<>();
    }

    public StartFrame(IControllerHost controller) {
        this.controller = controller;
        infos = new ArrayList<>();
    }
    
    @Override
    public void start(Stage primaryStage) {
        IconFontFX.register(GoogleMaterialDesignIcons.getIconFont());
        
        String stylesheet = getClass().getResource("beergamefx.css").toExternalForm();
        
        mainScene = new MainScene(controller, this);
        
        if (controller != null && controller instanceof Controller){
            ((Controller)controller).setHostGUI(this);
        }
        
        StackPane p = new StackPane();
        p.getChildren().add(0, new BubblePanel());
        p.getChildren().add(1, mainScene);
        
        this.primaryStage = primaryStage;
        primaryStage.setMinWidth(933 + 16);
        primaryStage.setMinHeight(700 + 39);
        
        Scene s = new Scene(p, 933, 700);
        
        s.getStylesheets().add(stylesheet);
        LocalizationUtils.bindLocalizationText(primaryStage.titleProperty(), HostLocalizationKeys.FRAME_NAME);
        primaryStage.setScene(s);
        primaryStage.getIcons().add(new Image(new File("icon" + File.separator + "Beer_mug_transparent2.png").toURI().toString()));
        
        this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                if(mainScene.isAnyGameRunning()){
                    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.MESSAGE_CLOSEAPP_WARN));
                    Label icon = new Label();
                    icon.getStyleClass().addAll("warning", "dialog-pane", "alert");
                    confirm.setGraphic(icon);
                    ((Stage)confirm.getDialogPane().getScene().getWindow()).getIcons().add(new Image(new File("icon" + File.separator + "Beer_mug_transparent2.png").toURI().toString()));
                    confirm.setHeaderText(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.MESSAGE_CLOSEAPP_WARN_TITLE));
                    confirm.setTitle(HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.MESSAGE_CLOSEAPP_WARN_TITLE));
                    Optional<ButtonType> res = confirm.showAndWait();

                    if (res.get() != ButtonType.OK) {
                        event.consume();
                        return;
                    }
                }

                for(Stage st : infos){
                    st.close();
                }
            }
        });
        
        primaryStage.show();
    }
    
    public void makeAlert(Node content, String title){
        String stylesheet = getClass().getResource("beergamefx.css").toExternalForm();

        StackPane p = new StackPane();
        p.getChildren().add(0, new BubblePanel());
        p.getChildren().add(1, content);
        
        Scene s = new Scene(p);
        s.getStylesheets().add(stylesheet);
        
        Stage window = new Stage();
        window.setTitle(title);
        window.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                infos.remove(window);
            }
        });
        
        infos.add(window);
        
        window.setScene(s);
        window.getIcons().add(new Image(new File("icon" + File.separator + "Beer_mug_transparent2.png").toURI().toString()));
        window.show();
    }

    @Override
    public void stop() throws Exception {
        controller.closeApplication();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void pushGameRoomUpdate(String gameName) {
        mainScene.updateGame(gameName);
    }
    
    public void runGUI(){
        launch();
    }
    
}
