/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import java.io.File;
import javafx.scene.image.Image;

/**
 *
 * @author henrique
 */
public class StaticImages {
    public static final Image RETAILER_ICON = new Image(new File("icon" + File.separator + "retailer.png").toURI().toString());
    public static final Image WHOLESALER_ICON = new Image(new File("icon" + File.separator + "wholesaler.png").toURI().toString());
    public static final Image DISTRIBUTOR_ICON = new Image(new File("icon" + File.separator + "distributor.png").toURI().toString());
    public static final Image PRODUCER_ICON = new Image(new File("icon" + File.separator + "Industry.png").toURI().toString());
}
