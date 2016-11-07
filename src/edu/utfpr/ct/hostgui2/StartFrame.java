/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import test.mock.ControllerMock;

/**
 *
 * @author henrique
 */
public class StartFrame extends Application {
    private MainScene mainScene;
    
    @Override
    public void start(Stage primaryStage) {
        IControllerHost control = new ControllerMock();
        mainScene = new MainScene(control);
        
        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(460);
        
        Scene s = new Scene(mainScene, 680, 460);
        s.getStylesheets().add(StartFrame.class.getResource("/edu/utfpr/ct/hostgui2/utils/default.css").toExternalForm());
        primaryStage.setTitle(Localize.getTextForKey(LocalizationKeys.FRAME_NAME));
        primaryStage.setScene(s);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
