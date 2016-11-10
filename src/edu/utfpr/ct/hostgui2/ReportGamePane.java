/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

/**
 *
 * @author henrique
 */
public class ReportGamePane extends BorderPane{
    private static final Image webImage = new Image(new File(Localize.getTextForKey(LocalizationKeys.WEB_ICON)).toURI().toString());
    
    private Game game;
    
    private Label gameName;
    private ToggleButton webStart;
    
    private Parent nodePane(Node node){
        return new ScrollPane();
    }
    
    private Parent generalPane(){
        return new ScrollPane();
    }
    
    private void createContent(){
        gameName = new Label(game.name);
        
        webStart = new ToggleButton();
        webStart.setGraphic(new ImageView(webImage));
        
        BorderPane topPane = new BorderPane();
        topPane.setCenter(gameName);
        topPane.setRight(webStart);
        
        TabPane reports = new TabPane();
        
        Tab general = new Tab(Localize.getTextForKey(LocalizationKeys.TITLE_REPORT_GENERAL));
        general.setContent(generalPane());
        general.setClosable(false);
        
        reports.getTabs().add(general);
        
        for(AbstractNode n : game.supplyChain){
            if(n instanceof Node){
                String s = "UNKNOW";
                
                switch(((Node) n).function.getPosition()){
                    case 1:
                        s = Localize.getTextForKey(LocalizationKeys.TITLE_REPORT_RETAILER);
                        break;
                    case 2:
                        s = Localize.getTextForKey(LocalizationKeys.TITLE_REPORT_WHOLESALER);
                        break;
                    case 3:
                        s = Localize.getTextForKey(LocalizationKeys.TITLE_REPORT_DISTRIBUTOR);
                        break;
                    case 4:
                        s = Localize.getTextForKey(LocalizationKeys.TITLE_REPORT_PRODUCER);
                        break;
                }
                
                Tab nodeTab = new Tab(s);
                nodeTab.setContent(nodePane((Node) n));
                nodeTab.setClosable(false);
                
                reports.getTabs().add(nodeTab);
            }
        }
        
        this.setTop(topPane);
        this.setCenter(reports);
    }

    public ReportGamePane(Game game, boolean isStreaming) {
        this.game = game;
        
        createContent();
        
        updateReport(game, isStreaming);
    }

    public void updateReport(Game game, boolean b) {
        webStart.setSelected(b);
    }
}
