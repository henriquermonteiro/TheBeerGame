package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.AbstractNode;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.Node;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.LocalizeHost;
import java.io.File;
import javafx.beans.value.ObservableValue;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class ReportGamePane extends BorderPane{
    private static final Image webImage = new Image(new File(LocalizeHost.getTextForKey(HostLocalizationKeys.WEB_ICON)).toURI().toString());
    
    private final Game game;
    
    private Label gameName;
    private ToggleButton webStart;

    private final MainScene mainScene;
    
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
        
        webStart.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            mainScene.changeReportState(game, newValue);
        });
        
        BorderPane topPane = new BorderPane();
        topPane.setCenter(gameName);
        topPane.setRight(webStart);
        
        TabPane reports = new TabPane();
        
        Tab general = new Tab(LocalizeHost.getTextForKey(HostLocalizationKeys.TITLE_REPORT_GENERAL));
        general.setContent(generalPane());
        general.setClosable(false);
        
        reports.getTabs().add(general);
        
        for(AbstractNode n : game.supplyChain){
            if(n instanceof Node){
                String s = "UNKNOW";
                
                switch(((Node) n).function.getPosition()){
                    case 1:
                        s = LocalizeHost.getTextForKey(HostLocalizationKeys.TITLE_REPORT_RETAILER);
                        break;
                    case 2:
                        s = LocalizeHost.getTextForKey(HostLocalizationKeys.TITLE_REPORT_WHOLESALER);
                        break;
                    case 3:
                        s = LocalizeHost.getTextForKey(HostLocalizationKeys.TITLE_REPORT_DISTRIBUTOR);
                        break;
                    case 4:
                        s = LocalizeHost.getTextForKey(HostLocalizationKeys.TITLE_REPORT_PRODUCER);
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

    public ReportGamePane(Game game, boolean isStreaming, MainScene mainScene) {
        this.game = game;
        this.mainScene = mainScene;
        
        createContent();
        
        updateReport(game, isStreaming);
    }

    public void updateReport(Game game, boolean b) {
        webStart.setSelected(b);
    }
}
