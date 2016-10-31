/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author henrique
 */
public class StartFrame extends Application {
    private MainScene mainScene = new MainScene();
    
    @Override
    public void start(Stage primaryStage) {
        Scene s = new Scene(mainScene, 600, 400);
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
