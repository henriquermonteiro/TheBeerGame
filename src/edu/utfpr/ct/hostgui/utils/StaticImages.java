package edu.utfpr.ct.hostgui.utils;

import java.io.File;
import javafx.scene.image.Image;

public class StaticImages {
    public static final Image RETAILER_ICON = new Image(new File("resources" + File.separator + "webapp" + File.separator + "resources" + File.separator + "retailer.png").toURI().toString());
    public static final Image WHOLESALER_ICON = new Image(new File("resources" + File.separator + "webapp" + File.separator + "resources" + File.separator + "wholesaler.png").toURI().toString());
    public static final Image DISTRIBUTOR_ICON = new Image(new File("resources" + File.separator + "webapp" + File.separator + "resources" + File.separator + "distributor.png").toURI().toString());
    public static final Image PRODUCER_ICON = new Image(new File("resources" + File.separator + "webapp" + File.separator + "resources" + File.separator + "Industry.png").toURI().toString());
    
    public static final Image BEERMUG2_ICON = new Image(new File("resources" + File.separator + "webapp" + File.separator + "resources" + File.separator + "Beer_mug_transparent2.png").toURI().toString());
}
