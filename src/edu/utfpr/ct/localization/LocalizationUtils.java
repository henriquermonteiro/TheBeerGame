package edu.utfpr.ct.localization;

import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;

public class LocalizationUtils {
    public static void bindLocalizationText(StringProperty element, String key){
        element.bind(Bindings.createStringBinding(() -> {
            return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(key);
        }, HostLocalizationManager.getInstance().getLang()));
    }
}
