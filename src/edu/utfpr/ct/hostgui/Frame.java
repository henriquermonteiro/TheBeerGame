/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.hostgui.panels.StartingPanel;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javax.swing.JFrame;

/**
 *
 * @author henrique
 */
public class Frame extends JFrame{

    public Frame() {
        super(Localize.getTextForKey(LocalizationKeys.FRAME_NAME));
        
        this.add(new StartingPanel());
        
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    
    public static void main(String ... args){
        new Frame();
    }
}
