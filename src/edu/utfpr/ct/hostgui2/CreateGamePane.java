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
import javafx.scene.text.TextAlignment;

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
        
        GridPane grid2 = new GridPane();
        
        Label l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_MISSINGUC));
        l.setTextAlignment(TextAlignment.RIGHT);
        
        grid2.add(l, 0, 0);
        
        NumberChooserFX missingUnitCost = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(missingUnitCost, 1, 0);
        
        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_STOCKUC));
        
        grid2.add(l, 0, 1);
        
        NumberChooserFX stockUnitCost = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(stockUnitCost, 1, 1);
        
        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP));
        
        grid2.add(l, 0, 2);
        
        NumberChooserFX sellingUnitProffit = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(sellingUnitProffit, 1, 2);
        
        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_REAL_DURATION));
        
        grid2.add(l, 0, 3);
        
        NumberChooserFX realDuration = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(realDuration, 1, 3);
        
        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INF_DURATION));
        
        grid2.add(l, 0, 4);
        
        NumberChooserFX informedDuration = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(informedDuration, 1, 4);
        
        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY));
        
        grid2.add(l, 0, 5);
        
        NumberChooserFX deliveryDelay = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(deliveryDelay, 1, 5);
        
        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK));
        
        grid2.add(l, 0, 6);
        
        NumberChooserFX initialStock = new NumberChooserFX("", 0.0, 100.0, 0.0);
        
        grid2.add(initialStock, 1, 6);
        
        GridPane grid3 = new GridPane();
        
        this.setLeft(grid1);
        this.setCenter(grid2);
        this.setRight(grid3);
    }
}
