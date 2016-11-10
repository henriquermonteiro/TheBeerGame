/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.DemandTypes;
import edu.utfpr.ct.datamodel.Function;
import edu.utfpr.ct.datamodel.Game;
import edu.utfpr.ct.datamodel.SupplyChainTypes;
import edu.utfpr.ct.hostgui2.utils.BorderedTitledPane;
import edu.utfpr.ct.hostgui2.utils.NumberChooserFX;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.io.File;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author henrique
 */
public class CreateGamePane extends BorderPane {

    private static final Image rIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.RETAILER_ICON)).toURI().toString());
    private static final Image wIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.WHOLESALER_ICON)).toURI().toString());
    private static final Image dIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.DISTRIBUTOR_ICON)).toURI().toString());
    private static final Image pIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.PRODUCER_ICON)).toURI().toString());

    private static final Image checkIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.CONFIRM_ICON)).toURI().toString());
    private static final Image cancelIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.CANCEL_ICON)).toURI().toString());
    private static final Image advancedIcon = new Image(new File(Localize.getTextForKey(LocalizationKeys.ADVANCED_ICON)).toURI().toString());

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
    
    private MainScene mainScene;

    public CreateGamePane(MainScene mainScene) {
        super();
        this.mainScene = mainScene;
        
        createAdvancedContent();
        updateChart();
        
        createSimpleContent();
        
        this.setCenter(simplePane);
    }
    
    private void changePane(boolean toAdvanced){
        if(toAdvanced){
            this.setCenter(advancedPane);
        }else{
            this.setCenter(simplePane);
        }
    }

    private Parent getDemandParametersBox(Object[] parameterDef) {
        if (parameterDef.length % 3 == 0) {
            FlowPane ret = new FlowPane();
            ret.setAlignment(Pos.CENTER);
            ret.setHgap(15.0);
            ret.setVgap(8.0);
            ret.setPadding(new Insets(15));

            parametersElements = new Node[parameterDef.length / 3];

            for (int k = 0; k < parameterDef.length; k += 3) {
                VBox v = new VBox(3.0);
                
                Label l = new Label(Localize.getTextForKey((String) parameterDef[k]));
                v.getChildren().add(l);

                if (parameterDef[k + 1] == Integer.class) {
                    Spinner spin = new Spinner();
                    spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, (Integer) parameterDef[k + 2]));

                    spin.valueProperty().addListener(new ChangeListener() {
                        @Override
                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                            updateChart();
                        }
                    });

                    v.getChildren().add(spin);
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
                    pHolder = rIcon;
                }

                if (n.function.equals(Function.WHOLESALER)) {
                    pHolder = wIcon;
                }

                if (n.function.equals(Function.DISTRIBUTOR)) {
                    pHolder = dIcon;
                }

                if (n.function.equals(Function.PRODUCER)) {
                    pHolder = pIcon;
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
                    pHolder = rIcon;
                }

                if (n.function.equals(Function.WHOLESALER)) {
                    pHolder = wIcon;
                }

                if (n.function.equals(Function.DISTRIBUTOR)) {
                    pHolder = dIcon;
                }

                if (n.function.equals(Function.PRODUCER)) {
                    pHolder = pIcon;
                }

                double mult = (elemRow - 0.5) - (Math.abs((k % (elemRow * 2)) - (elemRow - 0.5)));

                if (k > 0) {
                    if (k % elemRow == 0) {
                        double arrowW = imgL / 5;
//                        double arrowInitX = (k % (elemRow * 2) == 0 ? (5 + (imgL / 2)) : (w - (imgL / 2) - 5)) - (arrowW / 2);
                        double arrowInitX = (k % (elemRow * 2) == 0 ? (5 + (imgL / 2)) : ((elemRow * imgL) + ((elemRow-1) * (imgL/2) ) + 5 - (imgL / 2))) - (arrowW / 2);
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

    private void callCreation(Boolean isSimple){
        Game game = new Game();
        
        if(isSimple){
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
        }else{
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
    }
    
    private void createSimpleContent() {
        simpleName = new TextField();
        simpleInformedSupplyChain = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INFORMED_SC));
        simpleUsePassword = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK));
        simplePassword = new TextField();
        simpleDemandChart = new LineChart(new NumberAxis(), new NumberAxis());
        simpleChainCanvas = new Canvas() {
            @Override
            public boolean isResizable() {
                return true;
            }
        };
        
        simpleName.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));
        simplePassword.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD));
        simplePassword.setDisable(true);
        
        simpleUsePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (simpleUsePassword.isSelected()) {
                    simplePassword.setDisable(false);
                } else {
                    simplePassword.setText(null);
                    simplePassword.setDisable(true);
                }
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
        
        GridPane.setConstraints(simpleName, 0, 0);
        GridPane.setConstraints(simpleInformedSupplyChain, 0, 1);
        GridPane.setConstraints(simpleUsePassword, 0, 2);
        GridPane.setConstraints(simplePassword, 0, 3);
        GridPane.setConstraints(simpleDemandChart, 0, 4);
        
        RowConstraints r15 = new RowConstraints();
//        r15.setPercentHeight(15);
        
        RowConstraints r40 = new RowConstraints();
//        r40.setPercentHeight(40);
        r40.setVgrow(Priority.ALWAYS);
        
        grid1.getRowConstraints().addAll(r15, r15, r15, r15, r40);
        
        grid1.getChildren().addAll(simpleName, simpleInformedSupplyChain, simpleUsePassword, simplePassword, simpleDemandChart);
        
        GridPane grid2 = new GridPane();
        
        RowConstraints rC = new RowConstraints();
        rC.setPercentHeight(100);
        
        ColumnConstraints cC = new ColumnConstraints();
        cC.setPercentWidth(50);
        
        grid2.getColumnConstraints().addAll(cC, cC);
        grid2.getRowConstraints().addAll(rC);
        
        grid2.setPadding(new Insets(20));
        
        grid2.add(grid1, 0, 0);
        grid2.add(bP, 1, 0);
        
        FlowPane fP = new FlowPane(Orientation.HORIZONTAL);
        fP.setAlignment(Pos.CENTER);
        
        Button createButton = new Button();
        createButton.setStyle("-fx-base: #00FF00");
        createButton.setGraphic(new ImageView(checkIcon));
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                callCreation(Boolean.TRUE);
            }
        });
        
        Button cancelButton = new Button();
        cancelButton.setGraphic(new ImageView(cancelIcon));
        cancelButton.setStyle("-fx-base: #FF0000");
        
        Button advButton = new Button();
        advButton.setStyle("-fx-base: #934500");
        advButton.setGraphic(new ImageView(advancedIcon));
        advButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePane(true);
            }
        });
        
        fP.getChildren().addAll(createButton, cancelButton, advButton);
        
        simplePane = new BorderPane();
        simplePane.setCenter(grid2);
        simplePane.setBottom(fP);
    }
    
    private void createAdvancedContent() {
        GridPane grid1 = new GridPane();
        grid1.setPadding(new Insets(5.0));
        grid1.setVgap(10.0);

        nameField = new TextField();
        nameField.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));
        
        grid1.add(nameField, 0, 0);

        informedSupplyChain = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INFORMED_SC));

        grid1.add(informedSupplyChain, 0, 1);

        usePassword = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK));

        password = new PasswordField();
        password.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD));
        password.setDisable(true);

        usePassword.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (usePassword.isSelected()) {
                    password.setDisable(false);
                } else {
                    password.setText(null);
                    password.setDisable(true);
                }
            }
        });

        grid1.add(usePassword, 0, 2);
        grid1.add(password, 0, 3);

        demandTypeSelect = new ComboBox<>();
        demandTypeSelect.getItems().addAll(DemandTypes.values());
        demandTypeSelect.setValue(DemandTypes.SINGLE_STEP);

        grid1.add(demandTypeSelect, 0, 4);

        demandChart = new LineChart(new NumberAxis(), new NumberAxis());
        demandChart.setPrefSize(600, 300);
        demandChart.legendVisibleProperty().setValue(Boolean.FALSE);
        demandChart.setCreateSymbols(false);

        grid1.add(demandChart, 0, 5);

        parameterBox = new ScrollPane();
        grid1.add(parameterBox, 0, 6);
        
        ColumnConstraints cConsG1 = new ColumnConstraints();
        cConsG1.setFillWidth(true);
        grid1.getColumnConstraints().add(cConsG1);

        updateParameterBox();

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(2.0));

        Label l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_MISSINGUC));

        GridPane.setConstraints(l, 0, 0, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 0);

        missingUnitCost = new NumberChooserFX("", 0.0, 100.0, 1.0, 0.01);

        grid2.add(missingUnitCost, 1, 0);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_STOCKUC));

        GridPane.setConstraints(l, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 1);

        stockUnitCost = new NumberChooserFX("", 0.0, 100.0, 0.5, 0.01);

        grid2.add(stockUnitCost, 1, 1);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP));

        GridPane.setConstraints(l, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 2);

        sellingUnitProffit = new NumberChooserFX("", 0.0, 100.0, 0.0, 0.01);

        grid2.add(sellingUnitProffit, 1, 2);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_REAL_DURATION));

        GridPane.setConstraints(l, 0, 3, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 3);

        realDuration = new NumberChooserFX("", 0.0, 100.0, 40.0, 1.0);
        realDuration.addValuePropertyListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                updateChart();
            }
        });

        grid2.add(realDuration, 1, 3);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INF_DURATION));

        GridPane.setConstraints(l, 0, 4, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 4);

        informedDuration = new NumberChooserFX("", 0.0, 100.0, 60.0, 1.0);

        grid2.add(informedDuration, 1, 4);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY));

        GridPane.setConstraints(l, 0, 5, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 5);

        deliveryDelay = new NumberChooserFX("", 0.0, 100.0, 2.0, 1.0);

        grid2.add(deliveryDelay, 1, 5);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK));

        GridPane.setConstraints(l, 0, 6, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 6);

        initialStock = new NumberChooserFX("", 0.0, 100.0, 10.0, 1.0);

        grid2.add(initialStock, 1, 6);

        BorderPane grid3 = new BorderPane();

        supplyChainTypeSelect = new ComboBox<>();
        supplyChainTypeSelect.getItems().addAll(SupplyChainTypes.values());
        supplyChainTypeSelect.setValue(SupplyChainTypes.CLASSIC_CHAIN);
        supplyChainTypeSelect.setDisable(true);

        grid3.setTop(supplyChainTypeSelect);

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

        HBox buttonsBox = new HBox(10);
        buttonsBox.setAlignment(Pos.BASELINE_RIGHT);
        buttonsBox.setPadding(new Insets(10));

        ImageView iV = new ImageView(cancelIcon);
        iV.setPreserveRatio(true);
        iV.setFitHeight(46);
        Button cancelButton = new Button("", iV);
        cancelButton.setStyle("-fx-base: #FF0000");

        iV = new ImageView(checkIcon);
        iV.setPreserveRatio(true);
        iV.setFitWidth(60);
        Button confirmButton = new Button("", iV);
        confirmButton.setStyle("-fx-base: #00FF00");
        
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                changePane(false);
            }
        });
        
        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                callCreation(Boolean.FALSE);
            }
        });

        buttonsBox.getChildren().addAll(confirmButton, cancelButton);

        GridPane gP = new GridPane();

        ColumnConstraints cCons = new ColumnConstraints();
        cCons.setPercentWidth(35);

        ColumnConstraints cCons2 = new ColumnConstraints();
        cCons2.setPercentWidth(30);

        gP.getColumnConstraints().add(cCons);
        gP.getColumnConstraints().add(cCons2);
        gP.getColumnConstraints().add(cCons);
        
        RowConstraints rCons = new RowConstraints();
        rCons.setPercentHeight(100);

        gP.getRowConstraints().add(rCons);
        
        grid1.setAlignment(Pos.CENTER);
        grid2.setAlignment(Pos.CENTER);
        
        gP.add(grid1, 0, 0);
        gP.add(grid2, 1, 0);
        gP.add(grid3, 2, 0);
        
        gP.setGridLinesVisible(true);

        advancedPane = new BorderPane();
        advancedPane.setCenter(gP);
        advancedPane.setBottom(buttonsBox);
    }
}
