/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.hostgui2.utils.BorderedTitledPane;
import edu.utfpr.ct.hostgui2.utils.NumberChooserFX;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 *
 * @author henrique
 */
public class CreateGamePane  extends BorderPane{
    private CheckBox usePassword;
    private TextField password;

    public CreateGamePane() {
        super();
        createContent();
    }
    
    private void createContent(){
        GridPane grid1 = new GridPane();
        
        TextField nameField = new TextField();
        nameField.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));
        
        grid1.add(nameField, 0, 0);
        
        CheckBox informedSupplyChain = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INFORMED_SC));
        
        grid1.add(informedSupplyChain, 0, 1);
        
        VBox securityBox = new VBox(10.0);
        usePassword = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK));
        
        password = new PasswordField();
        password.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD));
        password.setDisable(true);
        
        usePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(usePassword.isSelected()){
                    password.setDisable(false);
                }else{
                    password.setText(null);
                    password.setDisable(true);
                }
            }
        });
        
        
        grid1.add(usePassword, 0, 2);
        grid1.add(password, 0, 3);
        
        NumberChooserFX missingUnitCost = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_MISSINGUC), 0.0, 100.0, 0.0);
        
        grid1.add(missingUnitCost, 1, 1);
        
        NumberChooserFX stockUnitCost = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_STOCKUC), 0.0, 100.0, 0.0);
        
        grid1.add(stockUnitCost, 1, 2);
        
        NumberChooserFX sellingUnitProffit = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP), 0.0, 100.0, 0.0);
        
        grid1.add(sellingUnitProffit, 1, 3);
        
        NumberChooserFX realDuration = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_REAL_DURATION), 0.0, 100.0, 0.0);
        
        grid1.add(realDuration, 1, 4);
        
        NumberChooserFX informedDuration = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INF_DURATION), 0.0, 100.0, 0.0);
        
        grid1.add(informedDuration, 1, 5);
        
        NumberChooserFX deliveryDelay = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY), 0.0, 100.0, 0.0);
        
        grid1.add(deliveryDelay, 1, 6);
        
        NumberChooserFX initialStock = new NumberChooserFX(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK), 0.0, 100.0, 0.0);
        
        grid1.add(initialStock, 1, 7);
        
        this.setCenter(grid1);
    }
}
