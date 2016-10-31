/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author henrique
 */
public class GameRecovery extends JPanel {

    private JLabel label;
    private Object value;
    private JButton recover;
    private JButton remove;
    
    private JPanel recoverP;
    private JPanel removeP;

    public GameRecovery(String label, Object value) {
        this.setBorder(new RoundedBorder(Color.DARK_GRAY));

        this.label = new JLabel(label);
        this.value = value;

        this.recover = new JButton(new ImageIcon(Localize.getTextForKey(LocalizationKeys.RELOAD_ICON)));
//        this.recover.setBorder(new RoundedBorder(Color.cyan));
        
        this.remove = new JButton(new ImageIcon(Localize.getTextForKey(LocalizationKeys.TRASH_ICON)));
//        this.remove.setBorder(new RoundedBorder(Color.red));
        this.remove.setBackground(Color.red);
        
        
        this.label.setVerticalTextPosition(JLabel.CENTER);
        this.label.setHorizontalAlignment(JLabel.CENTER);
        this.label.setBackground(Color.yellow);
        this.label.setOpaque(true);
        
        this.add(this.label);
        
        recoverP = new JPanel(new BorderLayout());
        recoverP.setBorder(new RoundedBorder(Color.cyan));
        recoverP.add(this.recover);
        
        this.add(recoverP);
        
        removeP = new JPanel(new BorderLayout());
        removeP.setBorder(new RoundedBorder(Color.red));
        removeP.add(this.remove);
        
        this.add(removeP);
        
        this.setLayout(new Layout());
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setBackground(Color.yellow);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(new GameRecovery("Teste", 10));

        f.setSize(200, 100);
        f.setVisible(true);
    }

    class Layout implements LayoutManager {

        @Override
        public void addLayoutComponent(String string, Component cmpnt) {
        }

        @Override
        public void removeLayoutComponent(Component cmpnt) {
        }

        @Override
        public Dimension preferredLayoutSize(Container cntnr) {
            Dimension lPS = label.getPreferredSize();
            Dimension recPS = recover.getPreferredSize();
            Dimension remPS = remove.getPreferredSize();
            
            return new Dimension(10 + lPS.width + 3 + recPS.width + remPS.width, 
                    10 + Math.max(lPS.height, Math.max(recPS.height, remPS.height)));
        }

        @Override
        public Dimension minimumLayoutSize(Container cntnr) {
            Dimension lMS = label.getMinimumSize();
            Dimension recMS = recover.getMinimumSize();
            Dimension remMS = remove.getMinimumSize();
            
            return new Dimension(10 + lMS.width + 3 + recMS.width + remMS.width, 
                    10 + Math.max(lMS.height, Math.max(recMS.height, remMS.height)));
        }

        @Override
        public void layoutContainer(Container cntnr) {
            Insets ins = cntnr.getInsets();
            
            int w1 = recover.getIcon().getIconWidth() + 20;
            int w2 = remove.getIcon().getIconWidth() + 20;
            
            int width = cntnr.getWidth() - recoverP.getPreferredSize().width - removeP.getPreferredSize().width - 10 - ins.left - ins.right;
            
            label.setBounds(0 + ins.left, 0 + ins.top, width, cntnr.getHeight() - ins.bottom - ins.top);
            
            recoverP.setBounds(width + 10, 0, recoverP.getPreferredSize().width, cntnr.getHeight());
            removeP.setBounds(cntnr.getWidth() - removeP.getMinimumSize().width, 0, removeP.getMinimumSize().width, cntnr.getHeight());
            
        }

    }
}
