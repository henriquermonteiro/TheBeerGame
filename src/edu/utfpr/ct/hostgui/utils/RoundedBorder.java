/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.AbstractBorder;

/**
 *
 * @author henrique
 */
public class RoundedBorder extends AbstractBorder{
    private Color c;

    public RoundedBorder(Color c) {
        this.c = c;
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public Insets getBorderInsets(Component cmpnt) {
        return new Insets(5, 5, 5, 5);
    }

    @Override
    public void paintBorder(Component cmpnt, Graphics grphcs, int i, int i1, int i2, int i3) {
        super.paintBorder(cmpnt, grphcs, i, i1, i2, i3);
        
        grphcs.setColor(c);
        
        Shape clip = grphcs.getClip();
        
        Area clip2 = new Area(new RoundRectangle2D.Double(i, i1, i2, i3, 25, 10));
        
        grphcs.setClip(clip2);
        
        Graphics2D g2d = (Graphics2D)grphcs;
        
        Stroke st = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2));
        grphcs.drawRoundRect(i + 1, i1 + 1, i2 - 2, i3 - 2, 25, 10);
        
        g2d.setStroke(st);
        grphcs.setClip(clip);
    }
    
}
