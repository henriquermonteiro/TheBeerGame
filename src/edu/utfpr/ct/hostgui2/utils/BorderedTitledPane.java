/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2.utils;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 *
 * @author henrique
 */
public class BorderedTitledPane extends StackPane {

    public BorderedTitledPane(String titleString, Node content) {
        Label title = new Label(" " + titleString + " ");
        title.getStyleClass().add("bordered-titled-title");
        StackPane.setAlignment(title, Pos.TOP_CENTER);

        StackPane contentPane = new StackPane();
        content.getStyleClass().add("bordered-titled-content");
        contentPane.getChildren().add(content);

        getStyleClass().add("bordered-titled-border");
        getChildren().addAll(title, contentPane);
    }
}
