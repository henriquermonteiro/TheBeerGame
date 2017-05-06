package edu.utfpr.ct.hostgui.utils;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class Tester extends Application{
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(new FlowPane(new GameComponent("Turma 43 F", GameComponent.LOAD))));
        primaryStage.show();
    }
}
