/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.panels;

import edu.utfpr.ct.datamodel.Game;
import javax.swing.JPanel;

/**
 *
 * @author henrique
 */
class GamePanel extends JPanel{
    private MainPanel mainPanel;

    public GamePanel(MainPanel mainPanel, Game game) {
        super();
        this.mainPanel = mainPanel;
    }
    
    

    public void pushUpdate(Game game) {
        
    }
    
}
