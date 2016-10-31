/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.panels;

import edu.utfpr.ct.datamodel.Game;
import java.awt.Component;
import javax.swing.JPanel;

/**
 *
 * @author henrique
 */
public class MainOptionsPanel extends JPanel{
    private MainPanel mainPanel;
    private JPanel recoverGamesPanel;

    public MainOptionsPanel(MainPanel mainPanel) {
        super();
        this.mainPanel = mainPanel;
    }
    
    private void updateRecoverGamePanel(){
        Game[] available = mainPanel.getAvailableGames();
        
        if(available.length > 0){
            for(int k = 0; k < available.length || k < 3; k++) recoverGamesPanel.add((Component)null);
        }
    }
}
