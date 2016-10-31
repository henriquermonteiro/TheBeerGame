/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author henrique
 */
public class CustomDoubleButtonOption extends JPanel {

    private JLabel text;
    private JLabel option1;
    private JLabel option2;

    private Boolean option1Pressed;
    private Boolean option2Pressed;

    private ArrayList<CustomDoubleButtonListenner> listenners;

    public CustomDoubleButtonOption(JLabel text, JLabel option1, JLabel option2) {
        super();

        this.setLayout(new Layout());

        this.text = text;
        this.option1 = option1;
        this.option2 = option2;

        option1Pressed = false;
        option2Pressed = false;

        listenners = new ArrayList<>();

        add(text);
        add(option1);
        add(option2);

        this.addMouseListener(new MouseAdapter() {
            private int identify(MouseEvent me) {
                Point p = me.getPoint();

                if (p.x > getWidth() - (option1.getMinimumSize().getWidth() + 21 + option2.getMinimumSize().getWidth())) {
                    if (p.x < getWidth() - 11 - option2.getMinimumSize().getWidth()) {
                        return 2;
                    } else {
                        return 3;
                    }
                }

                return 1;
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                switch (identify(me)) {
                    case 2:
                        option1Pressed = false;
                        repaint();
                        break;
                    case 3:
                        option2Pressed = false;
                        repaint();
                        break;
                }
            }

            @Override
            public void mousePressed(MouseEvent me) {
                switch (identify(me)) {
                    case 2:
                        option1Pressed = true;
                        repaint();
                        break;
                    case 3:
                        option2Pressed = true;
                        repaint();
                        break;
                }
            }

            @Override
            public void mouseClicked(MouseEvent me) {
                int k = identify(me);
                
                if (k != 1) {
                    for (CustomDoubleButtonListenner l : listenners) {
                        if(k == 2){
                            l.button1Clicked();
                        }else{
                            l.button2Clicked();
                        }
                    }
                }
            }
        });

        revalidate();
    }

    public void addButtonListener(CustomDoubleButtonListenner listen) {
        listenners.add(listen);
    }

    @Override
    protected void paintComponent(Graphics grphcs) {
        super.paintComponent(grphcs);

        Graphics2D g2d = (Graphics2D) grphcs;

//        g2d.setColor(Color.WHITE);
//        g2d.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 25, 25);
        g2d.setColor(Color.BLACK);

        Stroke s = g2d.getStroke();

        g2d.setStroke(new BasicStroke(2f));

        g2d.drawRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 25, 25);

        if (option2Pressed) {
            g2d.setPaint(new GradientPaint(0, 0, new Color(191, 69, 69), 0, (int) option1.getMinimumSize().getHeight(), new Color(189, 66, 66)));
        } else {
            g2d.setPaint(new GradientPaint(0, 0, new Color(247, 89, 89), 0, (int) option1.getMinimumSize().getHeight(), new Color(219, 81, 81)));
        }
        Area area = new Area(new Rectangle2D.Double(getWidth() - option2.getWidth() - 11, 2, option2.getWidth() / 2 + 6, getHeight() - 4));
        area.add(new Area(new RoundRectangle2D.Double(getWidth() - option2.getWidth() - 11, 2, option2.getWidth() + 10 + 1, getHeight() - 4, 25, 25)));

        g2d.fill(area);

        if (option1Pressed) {
            g2d.setPaint(new GradientPaint(0, 0, new Color(84, 155, 186), 0, (int) option1.getMinimumSize().getHeight(), new Color(73, 132, 158)));
        } else {
            g2d.setPaint(new GradientPaint(0, 0, new Color(115, 213, 255), 0, (int) option1.getMinimumSize().getHeight(), new Color(100, 185, 222)));
        }
        area = new Area(new RoundRectangle2D.Double(getWidth() - option1.getWidth() - 11 - option2.getWidth() - 10, 2, option1.getWidth() + 10, getHeight() - 4, 25, 25));
        area.add(new Area(new Rectangle2D.Double(getWidth() - option1.getWidth() / 2 - option2.getWidth() - 10 - 6, 2, option1.getWidth() / 2 + 6, getHeight() - 4)));

        g2d.fill(area);

        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(0.8f));
        g2d.drawRoundRect(getWidth() - option1.getWidth() - 11 - option2.getWidth() - 10, 2, option1.getWidth() + 10 + option2.getWidth() + 10, getHeight() - 4, 25, 25);
        g2d.drawLine(getWidth() - option2.getWidth() - 11, 2, getWidth() - option2.getWidth() - 11, getHeight() - 4);
        
        g2d.setStroke(s);
    }

    class Layout implements LayoutManager {

        @Override
        public void addLayoutComponent(String string, Component cmpnt) {
        }

        @Override
        public void removeLayoutComponent(Component cmpnt) {
        }

        @Override
        public Dimension preferredLayoutSize(Container cntnr) {
            Dimension textD = text.getPreferredSize();
            Dimension op1D = option1.getPreferredSize();
            Dimension op2D = option2.getPreferredSize();

            return new Dimension(textD.width + op1D.width + op2D.width + 4,
                    4 + Math.max(textD.height, Math.max(op1D.height, op2D.height)));
        }

        @Override
        public Dimension minimumLayoutSize(Container cntnr) {
            Dimension textD = text.getMinimumSize();
            Dimension op1D = option1.getMinimumSize();
            Dimension op2D = option2.getMinimumSize();

            return new Dimension(textD.width + op1D.width + op2D.width + 4,
                    4 + Math.max(textD.height, Math.max(op1D.height, op2D.height)));
        }

        @Override
        public void layoutContainer(Container cntnr) {
            int xT = cntnr.getWidth();

            int h = cntnr.getHeight();

            option2.setBounds((int) (cntnr.getWidth() - 5 - option2.getMinimumSize().getWidth()), (int) (h / 2 - option2.getMinimumSize().getHeight() / 2), (int) option2.getMinimumSize().getWidth(), (int) option2.getMinimumSize().getHeight());
            option1.setBounds((int) (cntnr.getWidth() - option1.getMinimumSize().getWidth() - 8 - option2.getMinimumSize().getWidth() - 5), (int) (h / 2 - option1.getMinimumSize().getHeight() / 2), (int) option1.getMinimumSize().getWidth(), (int) option1.getMinimumSize().getHeight());

            xT -= (int) (option1.getMinimumSize().getWidth() + 11 + option2.getMinimumSize().getWidth());

            text.setBounds(5 + (int) ((xT / 2) - (text.getMinimumSize().getWidth() / 2)), (int) (h / 2 - text.getMinimumSize().getHeight() / 2), (int) text.getMinimumSize().getWidth(), (int) text.getMinimumSize().getHeight());
        }

    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.getContentPane().setBackground(Color.yellow);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(new CustomDoubleButtonOption(new JLabel("Teste"), new JLabel(new ImageIcon(Localize.getTextForKey(LocalizationKeys.RELOAD_ICON))), new JLabel(new ImageIcon(Localize.getTextForKey(LocalizationKeys.TRASH_ICON)))));

        f.setSize(200, 80);
        f.setVisible(true);
    }
}
