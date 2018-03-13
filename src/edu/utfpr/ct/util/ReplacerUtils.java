/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.util;

import java.util.HashMap;

/**
 *
 * @author henrique
 */
public class ReplacerUtils {
    private static final HashMap<String, String> TUTORIAL_REPLACERS;
    static {
        TUTORIAL_REPLACERS = new HashMap<>();
        TUTORIAL_REPLACERS.put("<var:player-name>", "<%=name%>");
        TUTORIAL_REPLACERS.put("<var:player-producer>", "<span name=\"dlg_PRODUCER_name\">(***nome***)</span>");
        TUTORIAL_REPLACERS.put("<var:player-distributor>", "<span name=\"dlg_DISTRIBUTOR_name\">(***nome***)</span>");
        TUTORIAL_REPLACERS.put("<var:player-wholesaler>", "<span name=\"dlg_WHOLESALER_name\">(***nome***)</span>");
        TUTORIAL_REPLACERS.put("<var:player-retailer>", "<span name=\"dlg_RETAILER_name\">(***nome***)</span>");
        TUTORIAL_REPLACERS.put("<var:inf-duration>", "<span name=\"dlg_dur\">***</span>");
        TUTORIAL_REPLACERS.put("<var:init-stock>", "<span name=\"dlg_istock\">***</span>");
        TUTORIAL_REPLACERS.put("<var:init-incoming>", "<span name=\"dlg_incomming\">***</span>");
        TUTORIAL_REPLACERS.put("<var:direct-client>", "<span name=\"dlg_client\">***</span>");
        TUTORIAL_REPLACERS.put("<var:direct-supplier>", "<span name=\"dlg_supl\">***</span>");
        TUTORIAL_REPLACERS.put("<var:stock-cost>", "<span name=\"dlg_stockcost\">***</span>");
        TUTORIAL_REPLACERS.put("<var:missing-cost>", "<span name=\"dlg_misscost\">***</span>");
        TUTORIAL_REPLACERS.put("<var:delivery-delay>", "<span name=\"dlg_delay\">***</span>");
        
        
        TUTORIAL_REPLACERS.put("<structure:paragraph>", "<p>");
        TUTORIAL_REPLACERS.put("<structure:end-paragraph>", "</p>");
        TUTORIAL_REPLACERS.put("<structure:if-producer>", "<span id=\"dlg_b5_prod\">");
        TUTORIAL_REPLACERS.put("<structure:if-not-producer>", "<span id=\"dlg_b5_notprod\">");
        TUTORIAL_REPLACERS.put("<structure:end-if>", "</span>");
        TUTORIAL_REPLACERS.put("<structure:page>", "<div name=\"text_tutorial\" class=\"mySlide\">");
        TUTORIAL_REPLACERS.put("<structure:end-page>", "</div>");
    }
    
    public static String tutorialReplace(String input){
        String ret = input;
        
        if(input != null){
            for(String key : TUTORIAL_REPLACERS.keySet()){
                ret = ret.replace(key, TUTORIAL_REPLACERS.get(key));
            }
        }
        
        return ret;
    }
}
