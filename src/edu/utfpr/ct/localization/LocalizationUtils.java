package edu.utfpr.ct.localization;

import java.util.concurrent.Callable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;

public class LocalizationUtils {
    public static void bindLocalizationText(StringProperty element, String key){
        element.bind(Bindings.createStringBinding(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(key);
            }
        }, HostLocalizationManager.getInstance().getLang()));
    }
}
