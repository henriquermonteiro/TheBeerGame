package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.gamecontroller.Controller;
import edu.utfpr.ct.hostgui.utils.BubblePanel;
import edu.utfpr.ct.interfaces.IGUI;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.localization.LocalizationUtils;
import javafx.scene.layout.StackPane;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconFontFX;
//import test.mock.ControllerMock;

public class StartFrame extends Application implements IGUI{
    private MainScene mainScene;
    private IControllerHost controller;

    public StartFrame() {
        controller = Controller.getController();
    }

    public StartFrame(IControllerHost controller) {
        this.controller = controller;
    }
    
    @Override
    public void start(Stage primaryStage) {
        IconFontFX.register(GoogleMaterialDesignIcons.getIconFont());
        
        String stylesheet = getClass().getResource("beergamefx.css").toExternalForm();
        
        mainScene = new MainScene(controller);
        
        if (controller != null && controller instanceof Controller){
            ((Controller)controller).setHostGUI(this);
        }
        
        StackPane p = new StackPane();
        p.getChildren().add(0, new BubblePanel());
        p.getChildren().add(1, mainScene);
        
        primaryStage.setMinWidth(933 + 16);
        primaryStage.setMinHeight(700 + 39);
        
        Scene s = new Scene(p, 933, 700);
        
        s.getStylesheets().add(stylesheet);
        LocalizationUtils.bindLocalizationText(primaryStage.titleProperty(), HostLocalizationKeys.FRAME_NAME);
        primaryStage.setScene(s);
        primaryStage.show();
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
