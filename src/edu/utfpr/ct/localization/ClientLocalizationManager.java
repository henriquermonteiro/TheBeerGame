package edu.utfpr.ct.localization;

import java.io.File;
import java.io.FileFilter;
import java.util.HashMap;

public class ClientLocalizationManager {

    private static ClientLocalizationManager clm;

    private final HashMap<String, Localize> localizeMap;
    
    private final HashMap<String, String> langMap;

    private ClientLocalizationManager() {
        localizeMap = new HashMap<>();
        langMap = new HashMap<>();
        
        File f = new File("resources" + File.separator + "lang" + File.separator + "web");
        
        FileFilter fF = new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".map");
            }
        };

        if (f.isDirectory()) {
            for (File l : f.listFiles(fF)) {
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
            return localizeMap.get("default");
        }

        return localizeMap.get(lang);
    }

    public static String[] getValidLanguages() {
        return getInstance().localizeMap.keySet().toArray(new String[0]);
    }
}
