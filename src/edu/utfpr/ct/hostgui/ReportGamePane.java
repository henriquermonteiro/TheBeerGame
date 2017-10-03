package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.LocalizationUtils;
import edu.utfpr.ct.webclient.ActionService;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.WebView;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class ReportGamePane extends BorderPane{
    private final Game game;
    
    private Label gameName;
    private ToggleButton webStart;

    private final MainScene mainScene;
    private IconNode tabPlayIcon;
    
    private void createContent(){
        getStyleClass().add("transparent");
        
        gameName = new Label(game.name);
        
        tabPlayIcon = new IconNode(GoogleMaterialDesignIcons.PLAY_ARROW);
        tabPlayIcon.getStyleClass().addAll("icon", "small");
        
        webStart = new ToggleButton();

        IconNode web_icon = new IconNode(GoogleMaterialDesignIcons.CAST_CONNECTED);
        web_icon.getStyleClass().addAll("icon");
        webStart.setGraphic(web_icon);
        webStart.getStyleClass().addAll("play-pause");
        webStart.setTooltip(new Tooltip());
        webStart.getTooltip().textProperty().bind(Bindings.createStringBinding(() -> {
            return (webStart.selectedProperty().get() ? 
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_SHOW_REPO_BUTT_START) : 
                    HostLocalizationManager.getInstance().getClientFor(HostLocalizationManager.getInstance().getLang().get()).getTextFor(HostLocalizationKeys.TOOLTIP_SHOW_REPO_BUTT_PAUSE));
        }, HostLocalizationManager.getInstance().getLang(), webStart.selectedProperty()));
        
        webStart.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            mainScene.changeReportState(game, newValue);
            
            if(getParent() instanceof GamePane)
                ((GamePane)getParent()).getTab().setGraphic((newValue ? tabPlayIcon : null));
        });
        
        
        Hyperlink info = new Hyperlink();
        IconNode infoIcon = new IconNode(GoogleMaterialDesignIcons.INFO_OUTLINE);
        infoIcon.getStyleClass().addAll("icon");
        
        Tooltip infoTooltip = new Tooltip();
        LocalizationUtils.bindLocalizationText(infoTooltip.textProperty(), HostLocalizationKeys.TOOLTIP_SHOW_REPO_INFO);
        Tooltip.install(info, infoTooltip);
        
        info.setGraphic(infoIcon);
        info.setOnAction((event) -> {
            mainScene.makeGameInfo(game.name);
        });
        
        BorderPane topPane = new BorderPane();
        topPane.getStyleClass().addAll("card", "header", "shadowed-1");
        topPane.setCenter(gameName);
        topPane.setRight(webStart);
        topPane.setLeft(info);
        
        TabPane reports = new TabPane();
        reports.getStyleClass().addAll("main-tabs", "transparent", "center", TabPane.STYLE_CLASS_FLOATING);
        reports.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        WebView chartViewOrder = new WebView();
        try {
            chartViewOrder.getEngine().load("http://127.0.0.1:"+ActionService.getService().getPort()+"/info?order=true&game-name="+URLEncoder.encode(game.name, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        
        HostLocalizationManager.getInstance().getLang().addListener((observable) -> {
            chartViewOrder.getEngine().reload();
        });
        
        chartViewOrder.getStyleClass().addAll("chart-view");
        
        BorderPane chartContainer = new BorderPane(chartViewOrder);
        chartContainer.getStyleClass().addAll("card", "padded-10");
        
        BorderPane marginPane = new BorderPane(chartContainer);
        marginPane.getStyleClass().addAll("transparent", "padded-10");
        
        Tab chartsTab = new Tab();
        LocalizationUtils.bindLocalizationText(chartsTab.textProperty(), HostLocalizationKeys.TITLE_REPORT_ORDER);
        chartsTab.setContent(marginPane);
        chartsTab.setClosable(false);
        chartsTab.getStyleClass().add("transparent");
        chartsTab.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(chartsTab.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_SHOW_REPO_TAB_ORDER);
        
        reports.getTabs().add(chartsTab);
        
        WebView chartViewStock = new WebView();
        try {
            chartViewStock.getEngine().load("http://127.0.0.1:"+ActionService.getService().getPort()+"/info?stock=true&game-name="+URLEncoder.encode(game.name, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
        }
        
        HostLocalizationManager.getInstance().getLang().addListener((observable) -> {
            chartViewStock.getEngine().reload();
        });
        
        chartViewStock.getStyleClass().addAll("chart-view");
        
        chartContainer = new BorderPane(chartViewStock);
        chartContainer.getStyleClass().addAll("card", "padded-10");
        
        marginPane = new BorderPane(chartContainer);
        marginPane.getStyleClass().addAll("transparent", "padded-10");
        
        chartsTab = new Tab();
        LocalizationUtils.bindLocalizationText(chartsTab.textProperty(), HostLocalizationKeys.TITLE_REPORT_STOCK);
        chartsTab.setContent(marginPane);
        chartsTab.setClosable(false);
        chartsTab.getStyleClass().add("transparent");
        chartsTab.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(chartsTab.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_SHOW_REPO_TAB_STOCK);
        
        reports.getTabs().add(chartsTab);
        
        this.setTop(topPane);
        this.setCenter(reports);
        BorderPane.setMargin(topPane, new Insets(10.0, 10.0, 5.0, 10.0));
        BorderPane.setMargin(reports, new Insets(0.0, 10.0, 10.0, 10.0));
    }

    public ReportGamePane(Game game, boolean isStreaming, MainScene mainScene) {
        this.game = game;
        this.mainScene = mainScene;
        
        createContent();
        
        updateReport(game, isStreaming);
    }

    public final void updateReport(Game game, boolean b) {
        webStart.setSelected(b);
    }
}
