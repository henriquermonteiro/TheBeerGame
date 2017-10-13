package edu.utfpr.ct.hostgui;

import edu.utfpr.ct.datamodel.DemandTypes;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.SupplyChainTypes;
import edu.utfpr.ct.hostgui.utils.NumberChooserFX;
import edu.utfpr.ct.hostgui.utils.StaticImages;
import edu.utfpr.ct.localization.HostLocalizationKeys;
import edu.utfpr.ct.localization.HostLocalizationManager;
import edu.utfpr.ct.localization.LocalizationUtils;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconNode;

public class CreateGamePane extends BorderPane {
    
    private TextField nameField;
    private CheckBox informedSupplyChain;
    private CheckBox usePassword;
    private TextField password;
    private ScrollPane parameterBox;
    private LineChart demandChart;
    private ComboBox<DemandTypes> demandTypeSelect;
    private Node[] parametersElements;
    private NumberChooserFX missingUnitCost;
    private NumberChooserFX stockUnitCost;
    private NumberChooserFX sellingUnitProffit;
    private NumberChooserFX realDuration;
    private NumberChooserFX informedDuration;
    private NumberChooserFX deliveryDelay;
    private NumberChooserFX initialStock;
    private ComboBox<SupplyChainTypes> supplyChainTypeSelect;
    private Canvas chainCanvas;

    private TextField simpleName;
    private CheckBox simpleInformedSupplyChain;
    private CheckBox simpleUsePassword;
    private TextField simplePassword;
    private LineChart simpleDemandChart;
    private Canvas simpleChainCanvas;

    private BorderPane advancedPane;
    private BorderPane simplePane;

    private final MainScene mainScene;

    public CreateGamePane(MainScene mainScene) {
        super();
        this.mainScene = mainScene;

        createAdvancedContent();
        updateChart();

        createSimpleContent();

        this.setCenter(simplePane);
    }

    private void changePane(boolean toAdvanced) {
        if (toAdvanced) {
            this.setCenter(advancedPane);
        } else {
            this.setCenter(simplePane);
        }
    }

    private Parent getDemandParametersBox(Object[] parameterDef) {
        if (parameterDef.length % 3 == 0) {
            VBox ret = new VBox(8);
            ret.setPadding(new Insets(15));

            parametersElements = new Node[parameterDef.length / 3];

            for (int k = 0; k < parameterDef.length; k += 3) {
                VBox v = new VBox(3.0);

                Label l = new Label();
                LocalizationUtils.bindLocalizationText(l.textProperty(), (String) parameterDef[k]);
                v.getChildren().add(l);

                if (parameterDef[k + 1] == Integer.class) {
                    Spinner spin = new Spinner();
                    spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, (Integer) parameterDef[k + 2]));

                    spin.valueProperty().addListener((ObservableValue observable, Object oldValue, Object newValue) -> {
                        updateChart();
                    });

                    v.getChildren().add(spin);
                    
                    Tooltip tooltip = new Tooltip();
                    LocalizationUtils.bindLocalizationText(tooltip.textProperty(), (k == 0 ? HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DEM_SS_INITVAL : (k == 3? HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DEM_SS_FINALVAL : 
                            (k == 6 ? HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DEM_SS_STEPWEEK : "Error"))));
                    
                    Tooltip.install(v, tooltip);
                    
                    parametersElements[k / 3] = spin;
                }

                ret.getChildren().add(v);
            }

            return ret;
        }

        return new VBox();
    }

    private int[] getParameters() {
        int[] list = new int[parametersElements.length + 1];

        int k = 0;
        for (Node n : parametersElements) {
            if (n instanceof Spinner) {
                list[k++] = (Integer) ((Spinner) n).getValue();
            }
        }

        list[k] = (int) realDuration.getValue();

        return list;
    }

    private void updateChart() {
        demandChart.getData().clear();

        int[] demand = demandTypeSelect.getValue().getDemandForParameter(getParameters());

        XYChart.Series serie = new XYChart.Series();
        int k = 1;
        for (int i : demand) {
            serie.getData().add(new XYChart.Data<>(k++, i));
        }

        ((NumberAxis) demandChart.getXAxis()).setAutoRanging(false);
        ((NumberAxis) demandChart.getXAxis()).setLowerBound(1.0);
        ((NumberAxis) demandChart.getXAxis()).setUpperBound(demand.length);
        ((NumberAxis) demandChart.getXAxis()).setMinorTickCount(1);
        demandChart.getData().add(serie);
    }

    private void updateParameterBox() {
        parameterBox.setContent(getDemandParametersBox(demandTypeSelect.getValue().getParamametersType()));
    }

    private void updateCanvas(Canvas chainCanvas) {
        GraphicsContext context = chainCanvas.getGraphicsContext2D();

        context.clearRect(0, 0, chainCanvas.getWidth(), chainCanvas.getHeight());

        double w = chainCanvas.getWidth();
        double h = chainCanvas.getHeight();

        context.setFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.LIGHTGRAY), new Stop(0.5, Color.GRAY), new Stop(1.0, Color.DARKGRAY)));

        double imgL;
        edu.utfpr.ct.datamodel.Node[] nodes = supplyChainTypeSelect.getValue().getSupplyChainBasics();

        if (h / w > 1.25) {

            imgL = (h - 10) / (nodes.length + ((nodes.length - 1) * 0.5));

            int k = 0;
            for (edu.utfpr.ct.datamodel.Node n : nodes) {
                Image pHolder = null;
                if (n.function.equals(Function.RETAILER)) {
                    pHolder = StaticImages.RETAILER_ICON;
                }

                if (n.function.equals(Function.WHOLESALER)) {
                    pHolder = StaticImages.WHOLESALER_ICON;
                }

                if (n.function.equals(Function.DISTRIBUTOR)) {
                    pHolder = StaticImages.DISTRIBUTOR_ICON;
                }

                if (n.function.equals(Function.PRODUCER)) {
                    pHolder = StaticImages.PRODUCER_ICON;
                }

                if (k > 0) {
                    double initH = (k * imgL) + ((k - 1) * (imgL / 2)) + 5;
                    double aUnitH = (imgL - 4) / 4;

                    double arrowW = imgL / 5;
                    double arrowInitX = (w / 2) - (arrowW / 2);
                    double arrowXUnit = arrowW / 4;

                    context.fillPolygon(new double[]{
                        arrowInitX + (arrowXUnit * 1),
                        arrowInitX + (arrowXUnit * 3),
                        arrowInitX + (arrowXUnit * 3),
                        arrowInitX + (arrowXUnit * 4),
                        arrowInitX + (arrowXUnit * 2),
                        arrowInitX,
                        arrowInitX + (arrowXUnit * 1)
                    }, new double[]{
                        initH + 2,
                        initH + 2,
                        initH + 2 + aUnitH,
                        initH + 2 + aUnitH,
                        initH + ((imgL - 4) / 2),
                        initH + 2 + aUnitH,
                        initH + 2 + aUnitH,}, 7);
                }

                context.setGlobalAlpha(0.5);

                context.drawImage(pHolder, (w / 2) - (imgL / 2), 5 + (k * imgL) + (k * (imgL / 2)), imgL, imgL);

                context.setGlobalAlpha(1.0);

                k++;
            }

        } else {
            int elemRow = (int) Math.ceil(Math.sqrt(nodes.length));

            imgL = (Math.min(w, h) - 10) / (elemRow + ((elemRow - 1) * 0.5));

            int k = 0;
            for (edu.utfpr.ct.datamodel.Node n : nodes) {
                Image pHolder = null;
                if (n.function.equals(Function.RETAILER)) {
                    pHolder = StaticImages.RETAILER_ICON;
                }

                if (n.function.equals(Function.WHOLESALER)) {
                    pHolder = StaticImages.WHOLESALER_ICON;
                }

                if (n.function.equals(Function.DISTRIBUTOR)) {
                    pHolder = StaticImages.DISTRIBUTOR_ICON;
                }

                if (n.function.equals(Function.PRODUCER)) {
                    pHolder = StaticImages.PRODUCER_ICON;
                }

                double mult = (elemRow - 0.5) - (Math.abs((k % (elemRow * 2)) - (elemRow - 0.5)));

                if (k > 0) {
                    if (k % elemRow == 0) {
                        double arrowW = imgL / 5;
                        double arrowInitX = (k % (elemRow * 2) == 0 ? (5 + (imgL / 2)) : ((elemRow * imgL) + ((elemRow - 1) * (imgL / 2)) + 5 - (imgL / 2))) - (arrowW / 2);
                        double arrowXUnit = arrowW / 4;

                        double initH = (Math.round(k / elemRow) * imgL) + 5;
                        double aUnitH = (imgL - 4) / 4;

                        context.fillPolygon(new double[]{
                            arrowInitX + (arrowXUnit * 1),
                            arrowInitX + (arrowXUnit * 3),
                            arrowInitX + (arrowXUnit * 3),
                            arrowInitX + (arrowXUnit * 4),
                            arrowInitX + (arrowXUnit * 2),
                            arrowInitX,
                            arrowInitX + (arrowXUnit * 1)
                        }, new double[]{
                            initH + 2,
                            initH + 2,
                            initH + 2 + aUnitH,
                            initH + 2 + aUnitH,
                            initH + ((imgL - 4) / 2),
                            initH + 2 + aUnitH,
                            initH + 2 + aUnitH}, 7);
                    } else if ((k % (elemRow * 2)) - (elemRow - 0.5) > 0) {
                        double arrowInitX = ((k % elemRow) * imgL) + (((k - 1) % elemRow) * (imgL / 2)) + 5;
                        double arrowXUnit = (imgL - 4) / 4;

                        double arrowH = imgL / 5;
                        double initH = ((Math.floor(k / elemRow)) * (imgL * 0.5)) + ((Math.floor(k / elemRow) + 0.5) * imgL) - (arrowH / 2) + 5;
                        double aUnitH = arrowH / 4;

                        context.fillPolygon(new double[]{
                            arrowInitX + 2,
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + ((imgL - 4) / 2),
                            arrowInitX + ((imgL - 4) / 2),
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + 2 + arrowXUnit
                        }, new double[]{
                            initH + (aUnitH * 2),
                            initH,
                            initH + (aUnitH * 1),
                            initH + (aUnitH * 1),
                            initH + (aUnitH * 3),
                            initH + (aUnitH * 3),
                            initH + (aUnitH * 4)}, 7);
                    } else {
                        double arrowInitX = ((k % elemRow) * imgL) + (((k - 1) % elemRow) * (imgL / 2)) + 5;
                        double arrowXUnit = (imgL - 4) / 4;

                        double arrowH = imgL / 5;
                        double initH = ((Math.floor(k / elemRow)) * (imgL * 0.5)) + ((Math.floor(k / elemRow) + 0.5) * imgL) - (arrowH / 2) + 5;
                        double aUnitH = arrowH / 4;

                        context.fillPolygon(new double[]{
                            arrowInitX + 2,
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + ((imgL - 4) / 2),
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + 2 + arrowXUnit,
                            arrowInitX + 2
                        }, new double[]{
                            initH + (aUnitH * 1),
                            initH + (aUnitH * 1),
                            initH,
                            initH + (aUnitH * 2),
                            initH + (aUnitH * 4),
                            initH + (aUnitH * 3),
                            initH + (aUnitH * 3)}, 7);
                    }
                }

                context.setGlobalAlpha(0.5);

                context.drawImage(pHolder, 5 + (mult * (imgL * 1.5)), 5 + (Math.floor(k / elemRow) * (imgL * 1.5)), imgL, imgL);

                context.setGlobalAlpha(1.0);

                k++;
            }
        }

        context.setStroke(new LinearGradient(0.0, 0.0, 1.0, 1.0, true, CycleMethod.REPEAT, new Stop(0.0, Color.rgb(0, 0, 0, .4)), new Stop(1.0, Color.rgb(0, 0, 0, 0.8))));
        context.strokeRect(0, 0, w, h);

    }

    private void callCreation(Boolean isSimple) {
        Game game = new Game();

        if (isSimple) {
            game.name = simpleName.getText();
            game.password = (simpleUsePassword.isSelected() ? simplePassword.getText() : "");
            game.supplyChain = supplyChainTypeSelect.getSelectionModel().getSelectedItem().getSupplyChain(2);
            game.deliveryDelay = 2;
            game.informedChainSupply = simpleInformedSupplyChain.isSelected();
            game.informedDuration = 50;
            game.realDuration = 40;
            game.initialStock = 10;
            game.missingUnitCost = 1.0;
            game.stockUnitCost = 0.5;
            game.sellingUnitProfit = 0.0;
            game.unitiesOnTravel = 5;
            game.demand = DemandTypes.SINGLE_STEP.getDemandForParameter(new int[]{5, 10, 10, 40});
        } else {
            game.name = nameField.getText();
            game.password = (usePassword.isSelected() ? password.getText() : "");
            game.informedChainSupply = informedSupplyChain.isSelected();
            game.demand = demandTypeSelect.getSelectionModel().getSelectedItem().getDemandForParameter(getParameters());
            game.sellingUnitProfit = sellingUnitProffit.getValue();
            game.missingUnitCost = missingUnitCost.getValue();
            game.stockUnitCost = stockUnitCost.getValue();
            game.realDuration = (int) realDuration.getValue();
            game.informedDuration = (int) informedDuration.getValue();
            game.deliveryDelay = (int) deliveryDelay.getValue();
            game.initialStock = (int) initialStock.getValue();
            game.supplyChain = supplyChainTypeSelect.getSelectionModel().getSelectedItem().getSupplyChain(game.deliveryDelay);
            game.unitiesOnTravel = game.demand[0];
        }

        mainScene.createGame(game);
        clearFields();
    }

    private void createSimpleContent() {
        simpleName = new TextField();
        simpleName.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(simpleName.promptTextProperty(), HostLocalizationKeys.LABEL_CREATEGAME_NAME);
        LocalizationUtils.bindLocalizationText(simpleName.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_NAME);
        
        simpleInformedSupplyChain = new CheckBox();
        simpleInformedSupplyChain.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(simpleInformedSupplyChain.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INFORMED_SC);
        LocalizationUtils.bindLocalizationText(simpleInformedSupplyChain.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_ISINFO);
        
        simpleUsePassword = new CheckBox();
        simpleUsePassword.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(simpleUsePassword.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK);
        LocalizationUtils.bindLocalizationText(simpleUsePassword.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_USEPASSW);
        
        simplePassword = new TextField();
        simplePassword.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(simplePassword.promptTextProperty(), HostLocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD);
        LocalizationUtils.bindLocalizationText(simplePassword.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_PASSW);
        
        simpleDemandChart = new LineChart(new NumberAxis(), new NumberAxis());
        simpleChainCanvas = new Canvas() {
            @Override
            public boolean isResizable() {
                return true;
            }
        };

        simplePassword.setDisable(true);

        simpleUsePassword.setOnAction((ActionEvent event) -> {
            if (simpleUsePassword.isSelected()) {
                simplePassword.setDisable(false);
            } else {
                simplePassword.setText(null);
                simplePassword.setDisable(true);
            }
        });

        simpleDemandChart.setPrefSize(600, 300);
        simpleDemandChart.legendVisibleProperty().setValue(Boolean.FALSE);
        simpleDemandChart.setCreateSymbols(false);

        int[] demand = demandTypeSelect.getValue().getDemandForParameter(getParameters());

        XYChart.Series serie = new XYChart.Series();
        int k = 1;
        for (int i : demand) {
            serie.getData().add(new XYChart.Data<>(k++, i));
        }

        ((NumberAxis) simpleDemandChart.getXAxis()).setAutoRanging(false);
        ((NumberAxis) simpleDemandChart.getXAxis()).setLowerBound(1.0);
        ((NumberAxis) simpleDemandChart.getXAxis()).setUpperBound(demand.length);
        ((NumberAxis) simpleDemandChart.getXAxis()).setMinorTickCount(1);
        simpleDemandChart.getData().add(serie);
        
        Tooltip chartTooltip = new Tooltip();
        LocalizationUtils.bindLocalizationText(chartTooltip.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DEM_CHART);

        HBox b = new HBox();
        b.setMinSize(0, 0);

        simpleChainCanvas.widthProperty().bind(b.widthProperty());
        simpleChainCanvas.heightProperty().bind(b.heightProperty());

        simpleChainCanvas.widthProperty().addListener(observable -> updateCanvas(simpleChainCanvas));
        simpleChainCanvas.heightProperty().addListener(observable -> updateCanvas(simpleChainCanvas));

        b.getChildren().add(simpleChainCanvas);
        b.fillHeightProperty().setValue(Boolean.TRUE);

        BorderPane bP = new BorderPane(b);

        GridPane grid1 = new GridPane();
        grid1.setVgap(5.0);

        GridPane.setConstraints(simpleName, 0, 0);
        GridPane.setConstraints(simpleInformedSupplyChain, 0, 1);
        GridPane.setConstraints(simpleUsePassword, 0, 2);
        GridPane.setConstraints(simplePassword, 0, 3);
        GridPane.setConstraints(simpleDemandChart, 0, 4);

        RowConstraints r15 = new RowConstraints();

        RowConstraints r40 = new RowConstraints();
        r40.setVgrow(Priority.ALWAYS);

        grid1.getRowConstraints().addAll(r15, r15, r15, r15, r40);
        
        ColumnConstraints cC = new ColumnConstraints();
        cC.setFillWidth(true);
        cC.setHgrow(Priority.ALWAYS);
        
        grid1.getColumnConstraints().add(cC);

        grid1.getChildren().addAll(simpleName, simpleInformedSupplyChain, simpleUsePassword, simplePassword, simpleDemandChart);
        
        BorderPane namePane = new BorderPane(grid1);
        namePane.getStyleClass().addAll("shadowed-1", "card", "left");
        
        BorderPane chainPane = new BorderPane(bP);
        chainPane.getStyleClass().addAll("shadowed-1", "card", "right");

        GridPane grid2 = new GridPane();
        grid2.setHgap(10.0);

        RowConstraints rC = new RowConstraints();
        rC.setPercentHeight(100);

        cC = new ColumnConstraints();
        cC.setPercentWidth(50);

        grid2.getColumnConstraints().addAll(cC, cC);
        grid2.getRowConstraints().addAll(rC);

        grid2.setPadding(new Insets(20));

        grid2.add(namePane, 0, 0);
        grid2.add(chainPane, 1, 0);

        FlowPane fP = new FlowPane(Orientation.HORIZONTAL);
        fP.setAlignment(Pos.CENTER);
        fP.getStyleClass().addAll("creation-buttons");

        Button createButton = new Button();
        createButton.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(createButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_BUTTON_SACCEP);
        createButton.getStyleClass().addAll("create");
        IconNode create = new IconNode(GoogleMaterialDesignIcons.DONE);
        create.getStyleClass().addAll("icon");
        createButton.setGraphic(create);
        createButton.setOnAction((ActionEvent event) -> {
            callCreation(Boolean.TRUE);
        });

        Button cancelButton = new Button();
        cancelButton.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(cancelButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_BUTTON_SCLEAR);
        IconNode cancel = new IconNode(GoogleMaterialDesignIcons.CLEAR);
        cancel.getStyleClass().addAll("icon");
        cancelButton.setGraphic(cancel);
        cancelButton.getStyleClass().addAll("clear");
        cancelButton.setOnAction((ActionEvent event) -> {
            clearFields();
        });

        Button advButton = new Button();
        advButton.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(advButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_BUTTON_SADVAN);
        advButton.getStyleClass().addAll("config");
        IconNode conf = new IconNode(GoogleMaterialDesignIcons.BUILD);
        conf.getStyleClass().addAll("icon");
        advButton.setGraphic(conf);
        advButton.setOnAction((ActionEvent event) -> {
            changePane(true);
        });

        fP.getChildren().addAll(createButton, cancelButton, advButton);

        simplePane = new BorderPane();
        simplePane.setCenter(grid2);
        simplePane.setBottom(fP);
    }

    private void createAdvancedContent() {
        GridPane grid1 = new GridPane();
        grid1.setVgap(10.0);
        grid1.setAlignment(Pos.TOP_CENTER);
        
        ColumnConstraints cConsG1 = new ColumnConstraints();
        cConsG1.setFillWidth(true);
        cConsG1.setHgrow(Priority.ALWAYS);
        
        grid1.getColumnConstraints().add(cConsG1);

        nameField = new TextField();
        nameField.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(nameField.promptTextProperty(), HostLocalizationKeys.LABEL_CREATEGAME_NAME);
        LocalizationUtils.bindLocalizationText(nameField.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_NAME);

        grid1.add(nameField, 0, 0);

        informedSupplyChain = new CheckBox();
        informedSupplyChain.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(informedSupplyChain.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INFORMED_SC);
        LocalizationUtils.bindLocalizationText(informedSupplyChain.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_ISINFO);

        grid1.add(informedSupplyChain, 0, 1);

        usePassword = new CheckBox();
        usePassword.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(usePassword.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK);
        LocalizationUtils.bindLocalizationText(usePassword.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_USEPASSW);

        password = new PasswordField();
        password.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(password.promptTextProperty(), HostLocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD);
        LocalizationUtils.bindLocalizationText(password.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_PASSW);
        password.setDisable(true);

        usePassword.setOnAction((ActionEvent event) -> {
            if (usePassword.isSelected()) {
                password.setDisable(false);
            } else {
                password.setText(null);
                password.setDisable(true);
            }
        });

        grid1.add(usePassword, 0, 2);
        grid1.add(password, 0, 3);

        cConsG1 = new ColumnConstraints();
        cConsG1.setFillWidth(true);
        grid1.getColumnConstraints().add(cConsG1);
        
        GridPane grid4 = new GridPane();
        grid4.setVgap(10.0);
        
        cConsG1 = new ColumnConstraints();
        cConsG1.setFillWidth(true);
        cConsG1.setPercentWidth(35);
        
        ColumnConstraints cConsG2 = new ColumnConstraints();
        cConsG2.setFillWidth(true);
        cConsG2.setPercentWidth(65);
        
        grid4.getColumnConstraints().add(cConsG1);
        grid4.getColumnConstraints().add(cConsG2);

        demandTypeSelect = new ComboBox<>();
        demandTypeSelect.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(demandTypeSelect.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DEM_CHOOSER);
        demandTypeSelect.getItems().addAll(DemandTypes.values());
        demandTypeSelect.setValue(DemandTypes.SINGLE_STEP);

        grid4.add(demandTypeSelect, 0, 0, 1, 1);

        demandChart = new LineChart(new NumberAxis(), new NumberAxis());
        demandChart.setPrefSize(600, 300);
        demandChart.legendVisibleProperty().setValue(Boolean.FALSE);
        demandChart.setCreateSymbols(false);
        
        Tooltip chartTooltip = new Tooltip();
        LocalizationUtils.bindLocalizationText(chartTooltip.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DEM_CHART);

        grid4.add(demandChart, 1, 0, 1, 2);

        parameterBox = new ScrollPane();
        grid4.add(parameterBox, 0, 1, 1, 1);
        
        RowConstraints rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.SOMETIMES);
        
        grid4.getRowConstraints().add(rC);
        
        rC = new RowConstraints();
        rC.setFillHeight(true);
        rC.setVgrow(Priority.ALWAYS);
        grid4.getRowConstraints().add(rC);

        updateParameterBox();

        GridPane grid2 = new GridPane();
        grid2.setAlignment(Pos.TOP_CENTER);

        Label l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_MISSINGUC);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_MISSC);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 0, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 0);

        missingUnitCost = new NumberChooserFX("", 0.0, 3.0, 1.0, 0.01);
        Tooltip sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_MISSC);
        Tooltip.install(missingUnitCost, sliderT);

        grid2.add(missingUnitCost, 1, 0);

        l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_STOCKUC);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_STOCKC);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 1);

        stockUnitCost = new NumberChooserFX("", 0.0, 3.0, 0.5, 0.01);
        sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_STOCKC);
        Tooltip.install(stockUnitCost, sliderT);

        grid2.add(stockUnitCost, 1, 1);

        l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_SELLINGP);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_SELLP);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 2);

        sellingUnitProffit = new NumberChooserFX("", 0.0, 3.0, 0.0, 0.01);
        sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_SELLP);
        Tooltip.install(sellingUnitProffit, sliderT);

        grid2.add(sellingUnitProffit, 1, 2);

        l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_REAL_DURATION);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_REAL_DUR);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 3, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 3);

        realDuration = new NumberChooserFX("", 0.0, 100.0, 40.0, 1.0);
        sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_REAL_DUR);
        Tooltip.install(realDuration, sliderT);
        realDuration.addValuePropertyListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            updateChart();
        });

        grid2.add(realDuration, 1, 3);

        l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INF_DURATION);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_INF_DUR);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 4, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 4);

        informedDuration = new NumberChooserFX("", 0.0, 100.0, 60.0, 1.0);
        sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_INF_DUR);
        Tooltip.install(informedDuration, sliderT);

        grid2.add(informedDuration, 1, 4);
        
        informedDuration.addValuePropertyListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if(newValue.doubleValue() < realDuration.getValue()) realDuration.setValue(newValue.doubleValue());
        });
        
        realDuration.addValuePropertyListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if(newValue.doubleValue() > informedDuration.getValue()) informedDuration.setValue(newValue.doubleValue());
        });

        l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DELI_DELAY);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 5, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 5);

        deliveryDelay = new NumberChooserFX("", 0.0, 7.0, 2.0, 1.0);
        sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_DELI_DELAY);
        Tooltip.install(deliveryDelay, sliderT);

        grid2.add(deliveryDelay, 1, 5);

        l = new Label();
        l.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(l.textProperty(), HostLocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK);
        LocalizationUtils.bindLocalizationText(l.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_START_STOCK);
        l.setAlignment(Pos.CENTER_RIGHT);

        GridPane.setConstraints(l, 0, 6, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 6);

        initialStock = new NumberChooserFX("", 0.0, 100.0, 10.0, 1.0);
        sliderT = new Tooltip();
        LocalizationUtils.bindLocalizationText(sliderT.textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_START_STOCK);
        Tooltip.install(initialStock, sliderT);

        grid2.add(initialStock, 1, 6);
        
        cConsG1 = new ColumnConstraints();
        cConsG1.setHgrow(Priority.SOMETIMES);
        cConsG1.setFillWidth(true);
        
        cConsG2 = new ColumnConstraints();
        cConsG2.setHgrow(Priority.ALWAYS);
        cConsG2.setFillWidth(true);
        
        grid2.getColumnConstraints().addAll(cConsG1, cConsG2);

        BorderPane grid3 = new BorderPane();

        supplyChainTypeSelect = new ComboBox<>();
        supplyChainTypeSelect.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(supplyChainTypeSelect.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_INP_SCHAIN_CHOOSER);
        supplyChainTypeSelect.getItems().addAll(SupplyChainTypes.values());
        supplyChainTypeSelect.setValue(SupplyChainTypes.CLASSIC_CHAIN);
        supplyChainTypeSelect.setDisable(true);

        grid3.setTop(supplyChainTypeSelect);
        
        HostLocalizationManager.getInstance().getLang().addListener((observable) -> {
            DemandTypes typeD = demandTypeSelect.getValue();
            demandTypeSelect.getItems().clear();
            demandTypeSelect.getItems().addAll(DemandTypes.values());
            demandTypeSelect.setValue(typeD);
            
            SupplyChainTypes typeS = supplyChainTypeSelect.getValue();
            supplyChainTypeSelect.getItems().clear();
            supplyChainTypeSelect.getItems().addAll(SupplyChainTypes.values());
            supplyChainTypeSelect.setValue(typeS);
        });

        HBox b = new HBox();
        b.setMinSize(0, 0);

        chainCanvas = new Canvas() {
            @Override
            public boolean isResizable() {
                return true;
            }
        };

        chainCanvas.widthProperty().bind(b.widthProperty());
        chainCanvas.heightProperty().bind(b.heightProperty());

        chainCanvas.widthProperty().addListener(observable -> updateCanvas(chainCanvas));
        chainCanvas.heightProperty().addListener(observable -> updateCanvas(chainCanvas));

        b.getChildren().add(chainCanvas);
        b.fillHeightProperty().setValue(Boolean.TRUE);

        grid3.setCenter(b);

        updateCanvas(chainCanvas);
        
        BorderPane namePane = new BorderPane(grid1);
        namePane.getStyleClass().addAll("shadowed-1", "card", "top-left");
        
        BorderPane sliderPane = new BorderPane(grid2);
        sliderPane.getStyleClass().addAll("card", "shadowed-1", "center");
        
        BorderPane chainPane = new BorderPane(grid3);
        chainPane.getStyleClass().addAll("card", "shadowed-1", "right");
        
        BorderPane demandPane = new BorderPane(grid4);
        demandPane.getStyleClass().addAll("card", "shadowed-1", "bottom-left");

        FlowPane buttonsBox = new FlowPane(Orientation.HORIZONTAL);
        buttonsBox.setAlignment(Pos.BASELINE_CENTER);
        buttonsBox.getStyleClass().addAll("creation-buttons");

        IconNode cancel = new IconNode(GoogleMaterialDesignIcons.CLEAR);
        cancel.getStyleClass().addAll("icon");
        Button cancelButton = new Button("", cancel);
        cancelButton.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(cancelButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_BUTTON_ACLEAR);
        cancelButton.getStyleClass().addAll("clear");

        IconNode create = new IconNode(GoogleMaterialDesignIcons.DONE);
        create.getStyleClass().addAll("icon");
        Button confirmButton = new Button("", create);
        confirmButton.setTooltip(new Tooltip());
        LocalizationUtils.bindLocalizationText(confirmButton.getTooltip().textProperty(), HostLocalizationKeys.TOOLTIP_CREA_GAME_BUTTON_AACCEP);
        confirmButton.getStyleClass().addAll("create");

        cancelButton.setOnAction((ActionEvent event) -> {
            changePane(false);
            clearFields();
        });

        confirmButton.setOnAction((ActionEvent event) -> {
            callCreation(Boolean.FALSE);
        });

        buttonsBox.getChildren().addAll(confirmButton, cancelButton);

        GridPane gP = new GridPane();

        ColumnConstraints cCons = new ColumnConstraints();
        cCons.setPercentWidth(30);

        ColumnConstraints cCons2 = new ColumnConstraints();
        cCons2.setPercentWidth(50);
        
        ColumnConstraints cCons3 = new ColumnConstraints();
        cCons3.setPercentWidth(20);

        gP.getColumnConstraints().add(cCons);
        gP.getColumnConstraints().add(cCons2);
        gP.getColumnConstraints().add(cCons3);

        RowConstraints rCons = new RowConstraints();
        rCons.setVgrow(Priority.NEVER);
        gP.getRowConstraints().add(rCons);
        
        rCons = new RowConstraints();
        rCons.setVgrow(Priority.ALWAYS);
        gP.getRowConstraints().add(rCons);

        gP.add(namePane, 0, 0, 1, 1);
        gP.add(demandPane, 0, 1, 2, 1);
        gP.add(sliderPane, 1, 0, 1, 1);
        gP.add(chainPane, 2, 0, 1, 2);

        gP.setVgap(5.0);
        gP.setHgap(5.0);

        advancedPane = new BorderPane();
        advancedPane.setCenter(gP);
        advancedPane.setBottom(buttonsBox);
    }

    private void clearFields() {
        nameField.setText("");
        informedSupplyChain.setSelected(false);
        usePassword.setSelected(false);
        password.setText("");
        demandTypeSelect.setValue(DemandTypes.SINGLE_STEP);
        missingUnitCost.setValue(1.0);
        stockUnitCost.setValue(0.5);
        sellingUnitProffit.setValue(0.0);
        realDuration.setValue(40.0);
        informedDuration.setValue(60.0);
        deliveryDelay.setValue(2.0);
        initialStock.setValue(10.0);
        supplyChainTypeSelect.setValue(SupplyChainTypes.CLASSIC_CHAIN);
        updateParameterBox();

        simpleName.setText("");
        simpleInformedSupplyChain.setSelected(false);
        simpleUsePassword.setSelected(false);
        simplePassword.setText("");
    }
}
