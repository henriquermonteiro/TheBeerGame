package edu.utfpr.ct.hostgui.utils;

import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class LockedToggleButton extends ToggleButton{
//    private static Image locked = new Image(new File(Localize.getTextFor(HostLocalizationKeys.LOCKED_ICON)).toURI().toString());
//    private static Image unlocked = new Image(new File(Localize.getTextFor(HostLocalizationKeys.UNLOCKED_ICON)).toURI().toString());
            
    private IconNode icon;
    
    public LockedToggleButton() {
        super();
        
        icon = new IconNode(GoogleMaterialDesignIcons.LOCK_OPEN);
        
        this.setGraphic(icon);
        
        this.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
                    icon.setIconCode(GoogleMaterialDesignIcons.LOCK);
                }else{
                    icon.setIconCode(GoogleMaterialDesignIcons.LOCK_OPEN);
                }
            }
        });
    }
    
}
