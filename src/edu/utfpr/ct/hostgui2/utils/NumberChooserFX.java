/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 *
 * @author henrique
 */
public class NumberChooserFX extends HBox {

    private Label label;
    private Slider slider;
    private TextField text;
    private Boolean isDecimal;

    private boolean changing;

    public NumberChooserFX(String label, double min, double max, Double value, Double step) {
        this.isDecimal = (step * 10) % 10 != 0;

        this.label = new Label(label);
        this.slider = new Slider(min, max, value);
        this.text = new TextField(String.format((isDecimal ? "%.2f" : "%.0f"), value));

        this.text.setPrefWidth(50.0);

        changing = false;

        this.slider.setBlockIncrement(step);
        this.slider.setSnapToTicks(true);
        this.slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (!changing) {
                    changing = true;
                    text.setText(String.format((isDecimal ? "%.2f" : "%.0f"), newValue));
                    changing = false;
                }
            }
        });

        this.text.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!changing) {
                    changing = true;

                    try {
                        Double v = Double.parseDouble(text.getText());

                        v *= 100;
                        v = Math.round(v) / 100.0;

                        slider.setValue(v);

                    } catch (NumberFormatException e) {
                    }

                    text.setText(String.format((isDecimal ? "%.2f" : "%.0f"), slider.getValue()));

                    changing = false;
                }
            }
        });

        text.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    if (!changing) {
                        changing = true;

                        try {
                            Double v = Double.parseDouble(text.getText());

                            v *= 100;
                            v = Math.round(v) / 100.0;

                            slider.setValue(v);

                        } catch (NumberFormatException e) {
                        }

                        text.setText(String.format((isDecimal ? "%.2f" : "%.0f"), slider.getValue()));

                        changing = false;
                    }
                }
            }
        });

        this.setAlignment(Pos.CENTER);
        this.setSpacing(3.0);
        this.setPadding(new Insets(5.0, 5.0, 5.0, 5.0));

        this.getChildren().addAll(this.label, text, slider);
    }
    
    public void addValuePropertyListener(ChangeListener<? super Number> listener){
        slider.valueProperty().addListener(listener);
    }

    public double getValue() {
        return slider.getValue();
    }

}
