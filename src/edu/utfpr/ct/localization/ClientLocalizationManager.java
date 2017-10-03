package edu.utfpr.ct.localization;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import javafx.beans.property.SimpleStringProperty;

public class ClientLocalizationManager {

    private static ClientLocalizationManager clm;

    private final HashMap<String, Localize> localizeMap;
    
    private final HashMap<String, String> langMap;

    private ClientLocalizationManager() {
        localizeMap = new HashMap<>();
        langMap = new HashMap<>();
        
        File f = new File("lang" + File.separator + "web");

        if (f.isDirectory()) {
            for (File l : f.listFiles((File file, String string) -> { return string.endsWith(".map");} )) {
                    String lang = l.getName().substring(0, l.getName().length() - 4);
                    localizeMap.put(lang, new Localize(lang, false));
                    
                    langMap.put(localizeMap.get(lang).getTextFor(HostLocalizationKeys.LANGUAGE_NAME), lang);
            }
        }
    }

    public static ClientLocalizationManager getInstance() {
        if (clm == null) {
            clm = new ClientLocalizationManager();
        }

        return clm;
    }

    public Localize getClientFor(String lang) {
        if (!localizeMap.containsKey(lang)) {
            Localize loc = new Localize(lang, false);

            localizeMap.put(lang, loc);
        }

        return localizeMap.get(lang);
    }

    public static String[] getValidLanguages() {
//        File f = new File("lang" + File.separator + "web");
//
//        String[] langs = null;
//
//        if (f.isDirectory()) {
//            ArrayList<String> a_lang = new ArrayList<>();
//            
//            a_lang.add("en US");
//
//            for (File l : f.listFiles((File file, String string) -> { return string.endsWith(".map");} )) {
//                if (!l.getName().equals("default.map")) {
//                    a_lang.add(l.getName().replace(".map", ""));
//                }
//            }
//            
//            langs = a_lang.toArray(langs);
//        }

        return getInstance().localizeMap.keySet().toArray(new String[0]);
    }
    
//    public static String[] getValidLanguagesNames() {
//        File f = new File("lang");
//
//        String[] langs = null;
//
//        if (f.isDirectory()) {
//            ArrayList<String> a_lang = new ArrayList<>();
//            
//            a_lang.add("English");
//            
//            for (File l : f.listFiles((File file, String string) -> { return string.endsWith(".map");} )) {
//                if (!l.getName().equals("default.map")) {
//                    a_lang.add(new Locale(l.getName().replace(".map", "")).getDisplayLanguage());
//                }
//            }
//            
//            langs = a_lang.toArray(new String[0]);
//        }
//
//        return langs;
//    }
}
