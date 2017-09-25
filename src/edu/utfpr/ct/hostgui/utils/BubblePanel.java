package edu.utfpr.ct.hostgui.utils;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Node;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class BubblePanel extends Pane{

    public BubblePanel() {
        initialize();
    }

    public BubblePanel(Node... children) {
        super(children);
        initialize();
    }
    
    private void makeBubbles(int numb){
        for(int k = 0; k < numb; k++){
            CCircle c = new CCircle(Math.random(), Math.random(), 8 + (Math.random() * 12), Color.web("white", 0.1));
            c.setStrokeType(StrokeType.OUTSIDE);
            c.setStroke(Color.web("white", 0.2));
            c.setStrokeWidth(4);
            c.setEffect(new BoxBlur(6, 6, 2));
            c.setBlendMode(BlendMode.LIGHTEN);
            
            c.translateXProperty().bind(widthProperty().multiply(c.getPositionX()));
            c.translateYProperty().bind(heightProperty().multiply(c.getPositionY()));
            
            
            getChildren().add(c);
        }
       
    }

    private void initialize() {
        setWidth(800);
        setHeight(600);
        
        setStyle("-fx-background-color: linear-gradient(to bottom right, #FAE54A, #ED8E0E);");
        
        makeBubbles(100);
    }
    
    class CCircle extends Circle{
        private final DoubleProperty positionX;
        private final DoubleProperty positionY;

        public CCircle(double positionX, double positionY, double radius, Paint fill) {
            super(radius, fill);
            this.positionX = new SimpleDoubleProperty(positionX);
            this.positionY = new SimpleDoubleProperty(positionY);
        }

        public DoubleProperty getPositionX() {
            return positionX;
        }

        public DoubleProperty getPositionY() {
            return positionY;
        }
        
    }
}
