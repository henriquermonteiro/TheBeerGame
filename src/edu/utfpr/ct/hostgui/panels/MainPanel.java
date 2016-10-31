/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.panels;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.interfaces.IControllerHost;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.awt.BorderLayout;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 *
 * @author henrique
 */
public class MainPanel extends JPanel{
    private Map<Integer, GamePanel> runningGames;
    private IControllerHost control;
    private JTabbedPane tabbedPane;
    
    public MainPanel(IControllerHost control) {
        super(new BorderLayout());
        
        this.control = control;
        
        tabbedPane = new JTabbedPane(JTabbedPane.TOP);
        
        tabbedPane.addTab("", new ImageIcon(Localize.getTextForKey(LocalizationKeys.HOME_ICON)), new MainOptionsPanel(this));
        tabbedPane.addTab("", new ImageIcon(Localize.getTextForKey(LocalizationKeys.PLUS_ICON)), new CreateGamePanel(this));
        
        this.add(tabbedPane);
    }
    
    public void createGame(Game game){
        boolean success = control.createGame(game);
        
        if(success){
            tabbedPane.insertTab(game.name, null, new GamePanel(this, game), game.name, tabbedPane.getTabCount() - 1 );
        }else{
            JOptionPane.showInternalMessageDialog(this, Localize.getTextForKey(LocalizationKeys.MESSAGE_CREATEGAME_WARN_FAIL), Localize.getTextForKey(LocalizationKeys.MESSAGE_CREATEGAME_WARN_TITLE), JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public Game[] getAvailableGames(){
        return control.getUnfinishedGamesID();
    }

    public void pushGameRoomUpdate(Game game) {
        runningGames.get(game.gameID).pushUpdate(game);
    }
}
