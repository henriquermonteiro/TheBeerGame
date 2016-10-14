/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.panels;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author henrique
 */
public class StartingPanel extends JPanel{

    public StartingPanel() {
        super(new BorderLayout());
        
        JLabel labelWait = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_STARTING_WAIT));
        labelWait.setHorizontalAlignment(JLabel.CENTER);
        
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
                Container parent = ((JComponent)me.getSource()).getParent();
                parent.remove((JComponent)me.getSource());
                parent.add(new MainPanel());
                
                parent.revalidate();
                parent.repaint();
            }
        });
        
        this.add(labelWait);
    }
    
}
