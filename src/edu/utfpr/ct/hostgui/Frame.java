/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.hostgui.panels.MainPanel;
import edu.utfpr.ct.hostgui.panels.StartingPanel;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.interfaces.IGUI;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import javax.swing.JFrame;
import test.mock.ControllerMock;

/**
 *
 * @author henrique
 */
public class Frame extends JFrame implements IGUI{
    private IControllerHost host;
    private MainPanel mainPanel;
    
    public Frame(IControllerHost host) {
        super(Localize.getTextForKey(LocalizationKeys.FRAME_NAME));
        
        this.host = host;
        this.mainPanel = new MainPanel(host);
        
        this.add(new StartingPanel());
        
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    @Override
    public void pushGameRoomUpdate(Integer gameID) {
        mainPanel.pushGameRoomUpdate(host.getGameRoomData(gameID));
    }
    
    public void initializationReady(){
        this.getContentPane().removeAll();
        this.add(mainPanel);
        
        this.revalidate();
        this.repaint();
    }
    
    public static void main(String ... args){
        new Frame(new ControllerMock());
    }
}
