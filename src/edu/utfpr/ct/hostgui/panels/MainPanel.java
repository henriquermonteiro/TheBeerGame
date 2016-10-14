/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.panels;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author henrique
 */
public class MainPanel extends JPanel{

    public MainPanel() {
        super(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
        tabbedPane.addTab("", new ImageIcon(Localize.getTextForKey(LocalizationKeys.HOME_ICON)), new JPanel());
        tabbedPane.addTab("", new ImageIcon(Localize.getTextForKey(LocalizationKeys.PLUS_ICON)), new JPanel());
        
        this.add(tabbedPane);
    }
    
}
