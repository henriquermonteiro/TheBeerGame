
import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.Toolkit;

public class ShapeClipping {

    static Image image;

    public static void main(String[] args) {

// The image URL - change to where your image file is located!
        String imageURL = "image.png";

// This call returns immediately and pixels are loaded in the background
        image = Toolkit.getDefaultToolkit().getImage(imageURL);

// Create a frame
        Frame frame = new Frame();

// Add a component with a custom paint method
        frame.add(new CustomPaintComponent());

// Display the frame
        int frameWidth = 300;

        int frameHeight = 300;

        frame.setSize(frameWidth, frameHeight);

        frame.setVisible(true);

    }

    /*
    * To draw on the screen, it is first necessary to subclass a Component and
    * override its paint() method. The paint() method is automatically called
    * by the windowing system whenever component's area needs to be repainted.
     */
    static class CustomPaintComponent extends Component {

        public void paint(Graphics g) {

            // Retrieve the graphics context; this object is used to paint
            // shapes
            Graphics2D g2d = (Graphics2D) g;

            // Create an oval shape that's as large as the component
            int x = 0;

            int y = 0;

            int width = getSize().width - 1;

            int height = getSize().height - 1;

            Shape shape = new java.awt.geom.Ellipse2D.Float(x, y, width, height);

            // Set the clipping area
            g2d.setClip(shape);

            // Draw an image
//            g2d.drawImage(image, x, y, this);
            g2d.fillRect(x, y, width, height);

        }

    }

}
