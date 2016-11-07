/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.hostgui2.utils.NumberChooserFX;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author henrique
 */
public class GamePane extends BorderPane{
    private Game game;
    
    public GamePane(Game game) {
        super();
        this.game = game;
        
        createContent();
    }
    
    private void createContent(){
        GridPane grid1 = new GridPane();
        
        TextField nameField = new TextField();
        nameField.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));
        
        grid1.add(nameField, 0, 0);
        
//        NumberChooserFX sellingProffit = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP), 0.0, 100.0, 0.0);
//        
//        grid1.add(sellingProffit, 1, 1);
        
        this.setCenter(grid1);
    }
    
}
