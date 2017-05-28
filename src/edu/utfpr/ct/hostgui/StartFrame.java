package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.gamecontroller.ControllerHost;
import edu.utfpr.ct.interfaces.IGUI;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.utfpr.ct.interfaces.IControllerHost;
import java.net.URL;
//import test.mock.ControllerMock;

public class StartFrame extends Application implements IGUI{
    private MainScene mainScene;
    private IControllerHost controller;

    public StartFrame() {
        controller = ControllerHost.getControllerHost();
    }

    public StartFrame(IControllerHost controller) {
        this.controller = controller;
    }
    
    @Override
    public void start(Stage primaryStage) {
        mainScene = new MainScene(controller);
        
        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(460);
        
        Scene s = new Scene(mainScene, 680, 460);
        s.getStylesheets().add(StartFrame.class.getResource("/edu/utfpr/ct/hostgui/utils/default.css").toExternalForm());
        primaryStage.setTitle(Localize.getTextForKey(LocalizationKeys.FRAME_NAME));
        primaryStage.setScene(s);
        primaryStage.show();
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
    
}
