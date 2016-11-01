/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2;

import edu.utfpr.ct.datamodel.DemandTypes;
import edu.utfpr.ct.datamodel.Function;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

    private CheckBox usePassword;
    private TextField password;
    private ScrollPane parameterBox;
    private LineChart demandChart;
    private ComboBox<DemandTypes> demandTypeSelect;
    private Node[] parametersElements;

    private NumberChooserFX realDuration;

    private ComboBox<SupplyChainTypes> supplyChainTypeSelect;
    private Canvas chainCanvas;

    public CreateGamePane() {
        super();
        createContent();
        updateChart();
    }

    private Parent getDemandParametersBox(Object[] parameterDef) {
        if (parameterDef.length % 3 == 0) {
            VBox ret = new VBox(1.0);

            parametersElements = new Node[parameterDef.length / 3];

            for (int k = 0; k < parameterDef.length; k += 3) {
                Label l = new Label(Localize.getTextForKey((String) parameterDef[k]));
                ret.getChildren().add(l);

                if (parameterDef[k + 1] == Integer.class) {
                    Spinner spin = new Spinner();
                    spin.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, (Integer) parameterDef[k + 2]));

                    spin.valueProperty().addListener(new ChangeListener() {
                        @Override
                        public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                            updateChart();
                        }
                    });

                    ret.getChildren().add(spin);
                    parametersElements[k / 3] = spin;
                }
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

    private void updateCanvas() {
//        System.out.println(chainCanvas.getHeight());

        GraphicsContext context = chainCanvas.getGraphicsContext2D();

        context.clearRect(0, 0, chainCanvas.getWidth(), chainCanvas.getHeight());

//        context.setFill(new LinearGradient(0.0, 0.0, 0.0, 1.0, true, CycleMethod.REPEAT, new Stop(0.0, Color.rgb(0, 0, 0, 1.0)), new Stop(1.0, Color.rgb(255, 255, 255, 1.0))));
//        context.fillRect(0, 0, chainCanvas.getWidth(), chainCanvas.getHeight());
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

            imgL = (w - 10) / (elemRow + ((elemRow - 1) * 0.5));

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
                        double arrowInitX = (k % (elemRow * 2) == 0 ? (5 + (imgL / 2)) : (w - (imgL / 2) - 5)) - (arrowW / 2);
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

    private void createContent() {
        GridPane grid1 = new GridPane();
        grid1.setPadding(new Insets(5.0));
        grid1.setVgap(10.0);

        TextField nameField = new TextField();
        nameField.setPromptText(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));

        grid1.add(nameField, 0, 0);

        CheckBox informedSupplyChain = new CheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INFORMED_SC));

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
        demandChart.setPrefSize(100, 80);
        demandChart.setCreateSymbols(false);

        grid1.add(demandChart, 0, 5);

        parameterBox = new ScrollPane();
        grid1.add(parameterBox, 0, 6);

        updateParameterBox();

        GridPane grid2 = new GridPane();
        grid2.setPadding(new Insets(2.0));

        Label l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_MISSINGUC));

        GridPane.setConstraints(l, 0, 0, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 0);

        NumberChooserFX missingUnitCost = new NumberChooserFX("", 0.0, 100.0, 1.0, 0.01);

        grid2.add(missingUnitCost, 1, 0);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_STOCKUC));

        GridPane.setConstraints(l, 0, 1, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 1);

        NumberChooserFX stockUnitCost = new NumberChooserFX("", 0.0, 100.0, 0.5, 0.01);

        grid2.add(stockUnitCost, 1, 1);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP));

        GridPane.setConstraints(l, 0, 2, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 2);

        NumberChooserFX sellingUnitProffit = new NumberChooserFX("", 0.0, 100.0, 0.0, 0.01);

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

        NumberChooserFX informedDuration = new NumberChooserFX("", 0.0, 100.0, 60.0, 1.0);

        grid2.add(informedDuration, 1, 4);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY));

        GridPane.setConstraints(l, 0, 5, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 5);

        NumberChooserFX deliveryDelay = new NumberChooserFX("", 0.0, 100.0, 2.0, 1.0);

        grid2.add(deliveryDelay, 1, 5);

        l = new Label(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK));

        GridPane.setConstraints(l, 0, 6, 1, 1, HPos.RIGHT, VPos.CENTER);
        grid2.add(l, 0, 6);

        NumberChooserFX initialStock = new NumberChooserFX("", 0.0, 100.0, 10.0, 1.0);

        grid2.add(initialStock, 1, 6);

        BorderPane grid3 = new BorderPane();

//        GridPane grid3 = new GridPane();
//        grid3.setPadding(new Insets(2.0));
        supplyChainTypeSelect = new ComboBox<>();
        supplyChainTypeSelect.getItems().addAll(SupplyChainTypes.values());
        supplyChainTypeSelect.setValue(SupplyChainTypes.CLASSIC_CHAIN);

//        grid3.add(supplyChainTypeSelect, 0, 0);
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

        chainCanvas.widthProperty().addListener(observable -> updateCanvas());
        chainCanvas.heightProperty().addListener(observable -> updateCanvas());

        b.getChildren().add(chainCanvas);
        b.fillHeightProperty().setValue(Boolean.TRUE);

//        BorderPane canvasBox = new BorderPane(chainCanvas);
//        
//        GridPane.setConstraints(canvasBox, 0, 1, 1, 3, HPos.LEFT, VPos.BASELINE, Priority.ALWAYS, Priority.ALWAYS);
//        grid3.add(canvasBox, 0, 1);
        grid3.setCenter(b);

        updateCanvas();

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

        buttonsBox.getChildren().addAll(confirmButton, cancelButton);

        GridPane gP = new GridPane();

        ColumnConstraints cCons = new ColumnConstraints();
        cCons.setPercentWidth(30);

        ColumnConstraints cCons2 = new ColumnConstraints();
        cCons2.setPercentWidth(40);

        gP.getColumnConstraints().add(cCons);
        gP.getColumnConstraints().add(cCons2);
        gP.getColumnConstraints().add(cCons);

        gP.add(grid1, 0, 0);
        gP.add(grid2, 1, 0);
        gP.add(grid3, 2, 0);

//        this.setLeft(grid1);
//        this.setCenter(grid2);
//        this.setRight(grid3);
        this.setCenter(gP);

        this.setBottom(buttonsBox);
    }
}
