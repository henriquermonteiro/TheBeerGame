package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.gamecontroller.Controller;
import edu.utfpr.ct.hostgui.utils.BubblePanel;
import edu.utfpr.ct.interfaces.IGUI;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.LocalizeHost;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import edu.utfpr.ct.interfaces.IControllerHost;
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
        
        primaryStage.setMinWidth(680);
        primaryStage.setMinHeight(460);
        
        Scene s = new Scene(p, 680, 460);
//        Scene s = new Scene(mainScene, 680, 460);
//        s.getStylesheets().add(StartFrame.class.getResource("/edu/utfpr/ct/hostgui/utils/default.css").toExternalForm());
        s.getStylesheets().add(stylesheet);
        primaryStage.setTitle(LocalizeHost.getTextForKey(HostLocalizationKeys.FRAME_NAME));
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
    
    public void runGUI(){
        launch();
    }
    
}
