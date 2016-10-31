/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;

/**
 *
 * @author henrique
 */
public class NumberChooser extends JPanel {

    private JLabel label;
    private JSlider slider;
    private JTextField field;

    private Boolean changingFlag;
    
    private MyIntFilter filter;

    public NumberChooser(String label, Integer minValue, Integer maxValue, Integer initValue) {
        super();

        this.label = new JLabel(label);
        slider = new JSlider(minValue, maxValue, initValue);
        field = new JTextField();

        field.setText("" + slider.getValue());
        
        filter = new MyIntFilter(minValue, maxValue);

        changingFlag = false;

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                if (!changingFlag) {
                    changingFlag = true;
                    field.setText("" + slider.getValue());
                    ((PlainDocument)field.getDocument()).setDocumentFilter(filter);
                    changingFlag = false;
                }
            }
        });

        PlainDocument doc = (PlainDocument) field.getDocument();
        doc.setDocumentFilter(filter);

        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void changedUpdate(DocumentEvent e) {
                warn(e);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                warn(e);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                warn(e);
            }

            public void warn(DocumentEvent e) {
                if (!changingFlag) {
                    changingFlag = true;
                    try {
                        int val = Integer.parseInt(field.getText());

                        slider.setValue(val);

                    } catch (NumberFormatException nEx) {

                    }
                    changingFlag = false;
                }
            }
        });

        this.add(this.label);
        this.add(this.field);
        this.add(this.slider);

        this.setLayout(new LayoutManagerImp(this.label, slider, field));
    }

    public static void main(String... args) {
        JFrame f = new JFrame();
        f.setSize(300, 200);

        f.add(new NumberChooser("Chooser ...", 0, 500, 2));

        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    public int getValue() {
        return slider.getValue();
    }
}

class MyIntFilter extends DocumentFilter {

    private final Integer minValue;
    private final Integer maxValue;

    public MyIntFilter(Integer minValue, Integer maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public MyIntFilter() {
        this.minValue = Integer.MIN_VALUE;
        this.maxValue = Integer.MAX_VALUE;
    }

    @Override
    public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString()) || sb.length() == 0) {
//        if (test(sb.toString())) {
            super.insertString(fb, offset, string, attr);
        } else {
            // warn the user and don't allow the insert
        }
    }

    private boolean test(String text) {
        try {
            int v = Integer.parseInt(text);

            if (v >= minValue && v <= maxValue) {
                return true;
            }

        } catch (NumberFormatException e) {
        }
        
        return false;
    }

    @Override
    public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

//        if (test(sb.toString())) {
        if (test(sb.toString()) || sb.length() == 0) {
            super.replace(fb, offset, length, text, attrs);
        } else {
            // warn the user and don't allow the insert
        }

    }

    @Override
    public void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString()) || sb.length() == 0) {
//        if (test(sb.toString())) {
            super.remove(fb, offset, length);
        } else {
            // warn the user and don't allow the insert
        }

    }
}

class LayoutManagerImp implements LayoutManager {

    private JLabel label;
    private JSlider slider;
    private JTextField field;

    public LayoutManagerImp(JLabel label, JSlider slider, JTextField field) {
        this.label = label;
        this.slider = slider;
        this.field = field;
    }

    @Override
    public void addLayoutComponent(String string, Component cmpnt) {
    }

    @Override
    public void removeLayoutComponent(Component cmpnt) {
    }

    @Override
    public Dimension preferredLayoutSize(Container cntnr) {
        return new Dimension(label.getWidth() + field.getWidth() + slider.getWidth() + 4,
                Math.min(Math.min(label.getHeight(), field.getHeight()), slider.getHeight()));
    }

    @Override
    public Dimension minimumLayoutSize(Container cntnr) {
        return preferredLayoutSize(cntnr);
    }

    @Override
    public void layoutContainer(Container container) {
        label.setBounds(
                0,
                (int) ((container.getHeight() / 2) - (label.getMinimumSize().getHeight() / 2)),
                (int) label.getMinimumSize().getWidth(),
                (int) label.getMinimumSize().getHeight());

        field.setBounds(
                label.getWidth() + 2,
                (int) ((container.getHeight() / 2) - (field.getMinimumSize().getHeight() / 2)),
                60,
                (int) field.getMinimumSize().getHeight());

        slider.setBounds(
                label.getWidth() + field.getWidth() + 4,
                (int) ((container.getHeight() / 2) - (slider.getMinimumSize().getHeight() / 2)),
                container.getWidth() - (label.getWidth() + field.getWidth() + 4),
                (int) slider.getMinimumSize().getHeight());
    }

}
