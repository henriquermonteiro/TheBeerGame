/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2.utils;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author henrique
 */
public class LockedToggleButton extends ToggleButton{
    private static Image locked = new Image(new File(Localize.getTextForKey(LocalizationKeys.LOCKED_ICON)).toURI().toString());
    private static Image unlocked = new Image(new File(Localize.getTextForKey(LocalizationKeys.UNLOCKED_ICON)).toURI().toString());
            
    private ImageView icon;
    
    public LockedToggleButton() {
        super();
        
        icon = new ImageView(unlocked);
        
        this.setGraphic(icon);
        
        this.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    icon.setImage(locked);
                }else{
                    icon.setImage(unlocked);
                }
            }
        });
    }
    
}
