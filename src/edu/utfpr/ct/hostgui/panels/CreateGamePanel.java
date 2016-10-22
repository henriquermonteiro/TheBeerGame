/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.panels;

import edu.utfpr.ct.datamodel.Demand;
import edu.utfpr.ct.hostgui.utils.NumberChooser;
import edu.utfpr.ct.localization.LocalizationKeys;
import edu.utfpr.ct.localization.Localize;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 *
 * @author henrique
 */
public class CreateGamePanel extends JPanel{
    private JTextField nameField;
    private JTextField passwordField;
    private JCheckBox passwordCheckBox;
    private NumberChooser missingUnitCostChooser;
    private NumberChooser stockUnitCostChooser;
    private NumberChooser sellingUnitProffitChooser;
    private NumberChooser realDurationChooser;
    private NumberChooser informedDurationChooser;
    private JComboBox<Demand> demandComboBox;
    private JCheckBox informedSupplyChainCheckBox;
    private NumberChooser initialStockChooser;
    private NumberChooser deliveryDelayChooser;
    

    public CreateGamePanel() {
        super(new BorderLayout());
        
        nameField = new JTextField(Localize.getTextForKey(LocalizationKeys.DEFAULT_GAME_NAME));
        JLabel nameLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));
        nameLabel.setLabelFor(nameField);
        
        passwordField = new JTextField("");
        JLabel pwfieldLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD));
        pwfieldLabel.setLabelFor(passwordField);
        passwordField.setEnabled(false);
        passwordField.setEditable(false);
        
        passwordCheckBox = new JCheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK));
        passwordCheckBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent ce) {
                passwordField.setEnabled(passwordCheckBox.isSelected());
                passwordField.setEditable(passwordCheckBox.isSelected());
                passwordField.setText("");
            }
        });
        
        missingUnitCostChooser = new NumberChooser(
                "", 
                0, 100, 4);
        JLabel missingUnitCostLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_MISSINGUC));
        
        stockUnitCostChooser = new NumberChooser(
                "", 
                0, 100, 2);
        JLabel stockUnitCostLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_STOCKUC));
        
        sellingUnitProffitChooser = new NumberChooser(
                "", 
                0, 100, 0);
        JLabel sellingUnitProffitLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP));
        
        realDurationChooser = new NumberChooser(
                "", 
                10, 500, 30);
        JLabel realDurationLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_REAL_DURATION));
        
        informedDurationChooser = new NumberChooser(
                "", 
                5, 500, 50);
        JLabel informedDurationLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INF_DURATION));
        
        demandComboBox = new JComboBox<>();
        JLabel demandLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DEMAND));
        demandLabel.setLabelFor(demandComboBox);
        
        informedSupplyChainCheckBox = new JCheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INFORMED_SC));
        
        initialStockChooser = new NumberChooser(
                "", 
                0, 500, 10);
        JLabel initialStockLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK));
        
        deliveryDelayChooser = new NumberChooser(
                "", 
                0, 10, 2);
        JLabel deliveryDelayLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY));
        
//        JPanel center = new JPanel(new GridLayout(1, 3));
        JPanel center = new JPanel(new GridLayout(1, 3));
        JPanel lower = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        
        GridBagConstraints constraint = new GridBagConstraints();
        
        JPanel centerWest = new JPanel(new GridBagLayout());
        
        //Name Label
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.ipadx = 0;
        constraint.ipady = 0;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.weightx = 0.0;
        constraint.weighty = 0.1;
        
        centerWest.add(nameLabel, constraint);
        
        //Name Field
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.gridwidth = 2;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.weightx = 0.2;
        constraint.weighty = 0.00;
        
        centerWest.add(nameField, constraint);
        
        //Informed Chain
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 2;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.ipadx = 0;
        constraint.ipady = 0;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.weightx = 0.0;
        constraint.weighty = 0.00;
        
        centerWest.add(informedSupplyChainCheckBox, constraint);
        
        //Security Pane
        JPanel securityPane = new JPanel(new GridBagLayout());
        securityPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_SECURITY_BORDER)));
        
        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth = 3;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.2;
        constraint.weighty = 0.4;
        
        centerWest.add(securityPane, constraint);
        
        //Demand Pane
        JPanel demandPane = new JPanel();
        demandPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_DEMAND_BORDER)));
        
        constraint.gridx = 0;
        constraint.gridy = 3;
        constraint.gridwidth = 3;
        constraint.gridheight = 7;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.2;
        constraint.weighty = 0.5;
        
        centerWest.add(demandPane, constraint);
        
        JPanel centerMiddle = new JPanel(new GridBagLayout());
        
        //Costs Pane
        JPanel costPane = new JPanel(new GridBagLayout());
        costPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_COSTS_BORDER)));
        
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 3;
        constraint.gridheight = 4;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.05;
        
        centerMiddle.add(costPane, constraint);
        
        //Duration Pane
        JPanel durationPane = new JPanel(new GridBagLayout());
        durationPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_DURATION_BORDER)));
        
        constraint.gridx = 0;
        constraint.gridy = 4;
        constraint.gridwidth = 3;
        constraint.gridheight = 3;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.05;
        
        centerMiddle.add(durationPane, constraint);
        
        //Delivery Delay
        constraint.gridx = 0;
        constraint.gridy = 7;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 0.05;
        
        centerMiddle.add(deliveryDelayLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 7;
        constraint.gridwidth = 2;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.05;
        
        centerMiddle.add(deliveryDelayChooser, constraint);
        
        //Initial Stock
        constraint.gridx = 0;
        constraint.gridy = 8;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 0.1;
        
        centerMiddle.add(initialStockLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 8;
        constraint.gridwidth = 2;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.1;
        
        centerMiddle.add(initialStockChooser, constraint);
        
        //Supply Pane
        JPanel supplyChainPane = new JPanel(new BorderLayout());
        supplyChainPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_SUPPLYCHAIN_BORDER)));
        
        constraint.gridx = 6;
        constraint.gridy = 0;
        constraint.gridwidth = 3;
        constraint.gridheight = 10;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.4;
        constraint.weighty = 1.0;
        
        center.add(supplyChainPane);
        
        // SETUP COSTS PANEL
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth =  1;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 1.0;
        
        costPane.add(sellingUnitProffitLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1.0;
        constraint.weighty = 1.0;
        
        costPane.add(sellingUnitProffitChooser, constraint);
        
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth =  1;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 1.0;
        
        costPane.add(missingUnitCostLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1.0;
        constraint.weighty = 1.0;
        
        costPane.add(missingUnitCostChooser, constraint);
        
        
        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth =  1;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 1.0;
        
        costPane.add(stockUnitCostLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 2;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1.0;
        constraint.weighty = 1.0;
        
        costPane.add(stockUnitCostChooser, constraint);
        
        //SETUP Duration
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth =  1;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 1.0;
        
        durationPane.add(realDurationLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1.0;
        constraint.weighty = 1.0;
        
        durationPane.add(realDurationChooser, constraint);
        
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth =  1;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 1.0;
        
        durationPane.add(informedDurationLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 1.0;
        constraint.weighty = 1.0;
        
        durationPane.add(informedDurationChooser, constraint);
        
        //SETUP Security
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 0.0;
        
        securityPane.add(passwordCheckBox, constraint);
        
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth =  1;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.NONE;
        constraint.weightx = 0.0;
        constraint.weighty = 0.0;
        
        securityPane.add(pwfieldLabel, constraint);
        
        constraint.gridx = 1;
        constraint.gridy = 1;
        constraint.gridwidth =  2;
        constraint.gridheight =  1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.weightx = 1.0;
        constraint.weighty = 0.0;
        
        securityPane.add(passwordField, constraint);
        
        
        center.add(centerWest);
        center.add(centerMiddle);
        center.add(supplyChainPane);
        
        this.add(center);
        
        JButton confirm = new JButton();
        confirm.setBackground(Color.GREEN);
        confirm.setPreferredSize(new Dimension(90, 30));
        
        JButton cancel = new JButton();
        cancel.setBackground(Color.RED);
        cancel.setPreferredSize(new Dimension(90, 30));
        
        lower.add(cancel);
        lower.add(confirm);
        
        this.add(lower, BorderLayout.SOUTH);
    }
    
}
