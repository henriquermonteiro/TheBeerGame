/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.localization;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

/**
 *
 * @author henrique
 */
public class ClientLocalizationManager {

    private static ClientLocalizationManager clm;

    private final HashMap<String, LocalizeClient> localizeMap;

    private ClientLocalizationManager() {
        localizeMap = new HashMap<>();
    }

    public static ClientLocalizationManager getInstance() {
        if (clm == null) {
            clm = new ClientLocalizationManager();
        }

        return clm;
    }

    public LocalizeClient getClientFor(String lang) {
        if (!localizeMap.containsKey(lang)) {
//            try{
            LocalizeClient loc = new LocalizeClient();
            loc.changeLanguage(lang);

            localizeMap.put(lang, loc);
//            }catch(IllegalArgumentException ex){
//                
//            }
        }

        return localizeMap.get(lang);
    }

    public static String[] getValidLanguages() {
        File f = new File("lang");

        String[] langs = null;

        if (f.isDirectory()) {
            ArrayList<String> a_lang = new ArrayList<>();
            
            a_lang.add("en US");

            for (File l : f.listFiles((File file, String string) -> { return string.endsWith(".map");} )) {
                if (!l.getName().equals("default.map")) {
                    a_lang.add(l.getName().replace(".map", ""));
                }
            }
            
            langs = a_lang.toArray(langs);
        }

        return langs;
    }
    
    public static String[] getValidLanguagesNames() {
        File f = new File("lang");

        String[] langs = null;

        if (f.isDirectory()) {
            ArrayList<String> a_lang = new ArrayList<>();
            
            a_lang.add("English");
            
            for (File l : f.listFiles((File file, String string) -> { return string.endsWith(".map");} )) {
                if (!l.getName().equals("default.map")) {
                    a_lang.add(new Locale(l.getName().replace(".map", "")).getDisplayLanguage());
                }
            }
            
            langs = a_lang.toArray(new String[0]);
        }

        return langs;
    }
}
