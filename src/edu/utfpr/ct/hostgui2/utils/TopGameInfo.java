/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2.utils;

import edu.utfpr.ct.datamodel.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;

/**
 *
 * @author henrique
 */
public class TopGameInfo extends GridPane{
    private Node node;
    private TextField field;
    private ToggleButton lockedButton;
    private Label icon;

    public TopGameInfo(Node node) {
        this.node = node;
        
        field = new TextField(node.playerName);
    }
    
}
