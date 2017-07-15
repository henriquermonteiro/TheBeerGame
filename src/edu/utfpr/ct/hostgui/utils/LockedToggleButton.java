package edu.utfpr.ct.hostgui.utils;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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