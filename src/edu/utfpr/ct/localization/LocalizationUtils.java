package edu.utfpr.ct.localization;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author henrique
 */
public class LocalizationUtils {
    public static void bindLocalizationText(StringProperty element, String key){
        element.bind(Bindings.createStringBinding(() -> {
            return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(key);
        }, HostLocalizationManager.getInstance().getLang()));
    }
}
