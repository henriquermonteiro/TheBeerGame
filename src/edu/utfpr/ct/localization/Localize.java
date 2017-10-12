package edu.utfpr.ct.localization;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Locale;

public class Localize {

    private final String language;
    private HashMap<String, String> stringMapping;
    private final String path;

    public Localize(String lang, boolean isHost) {
        language = (lang.equalsIgnoreCase("default") ? Locale.getDefault().getLanguage() : lang);

        if (isHost) {
            path = "lang" + File.separator;
        } else {
            path = "lang" + File.separator + "web" + File.separator;
        }
        
        loadLanguage(lang);
    }

    public String getLanguage() {
        return language;
    }

    public String getTextFor(String keyString) {
        String ret = stringMapping.get(keyString);

        if (ret == null) {
            ret = "Error when tried to grab String, check Localization package or language definition file.";
        }

        return ret;
    }

    private void loadLanguage(String language) {
        File f = new File(path + language + ".map");

        if (!f.exists()) {
            f = new File(path + "default.map");
        }

        stringMapping = new HashMap<>();
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(f);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

            String s;
            while ((s = reader.readLine()) != null) {
                String[] ss = s.split("<>");

                if (ss.length == 2) {
                    stringMapping.put(ss[0], ss[1]);
                }
            }

        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException ex) {
                }
            }
        }
    }
}
