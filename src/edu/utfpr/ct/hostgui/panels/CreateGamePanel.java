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
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

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
    private NumberChooser initialStock;
    private NumberChooser deliveryDelay;
    

    public CreateGamePanel() {
        super(new BorderLayout());
        
        nameField = new JTextField(Localize.getTextForKey(LocalizationKeys.DEFAULT_GAME_NAME));
        JLabel nameLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_NAME));
        nameLabel.setLabelFor(nameField);
        
        passwordField = new JTextField("");
        JLabel pwfieldLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_FIELD));
        pwfieldLabel.setLabelFor(passwordField);
        
        passwordCheckBox = new JCheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_PASSWORD_CHECK));
        
        missingUnitCostChooser = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_MISSINGUC), 
                0, 100, 4);
        
        stockUnitCostChooser = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_STOCKUC), 
                0, 100, 2);
        
        sellingUnitProffitChooser = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_SELLINGP), 
                0, 100, 0);
        
        realDurationChooser = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_REAL_DURATION), 
                10, 500, 30);
        
        informedDurationChooser = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INF_DURATION), 
                5, 500, 50);
        
        demandComboBox = new JComboBox<>();
        JLabel demandLabel = new JLabel(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DEMAND));
        demandLabel.setLabelFor(demandComboBox);
        
        informedSupplyChainCheckBox = new JCheckBox(Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INFORMED_SC));
        
        initialStock = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_INITIAL_STOCK), 
                0, 500, 10);
        
        deliveryDelay = new NumberChooser(
                Localize.getTextForKey(LocalizationKeys.LABEL_CREATEGAME_DELIVERY_DELAY), 
                0, 10, 2);
        
//        JPanel center = new JPanel(new GridLayout(1, 3));
        JPanel center = new JPanel(new GridBagLayout());
        JPanel lower = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        
        GridBagConstraints constraint = new GridBagConstraints();
        
        //Name Label
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipadx = 0;
        constraint.ipady = 0;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.weightx = 0.0;
        constraint.weighty = 0.1;
        
        center.add(nameLabel, constraint);
        
        //Name Field
        constraint.gridx = 1;
        constraint.gridy = 0;
        constraint.gridwidth = 2;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.weightx = 0.2;
        constraint.weighty = 0.05;
        
        center.add(nameField, constraint);
        center.setBackground(Color.yellow);
        
        //Informed Chain
        constraint.gridx = 0;
        constraint.gridy = 1;
        constraint.gridwidth = 3;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.HORIZONTAL;
        constraint.ipadx = 0;
        constraint.ipady = 0;
        constraint.anchor = GridBagConstraints.CENTER;
        constraint.weightx = 0.2;
        constraint.weighty = 0.05;
        
        center.add(informedSupplyChainCheckBox, constraint);
        
        //Security Pane
        JPanel securityPane = new JPanel();
        securityPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_SECURITY_BORDER)));
        
        constraint.gridx = 0;
        constraint.gridy = 2;
        constraint.gridwidth = 3;
        constraint.gridheight = 3;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.2;
        constraint.weighty = 0.4;
        
        center.add(securityPane, constraint);
        
        //Demand Pane
        JPanel demandPane = new JPanel();
        demandPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_DEMAND_BORDER)));
        
        constraint.gridx = 0;
        constraint.gridy = 5;
        constraint.gridwidth = 3;
        constraint.gridheight = 5;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.2;
        constraint.weighty = 0.5;
        
        center.add(demandPane, constraint);
        
        //Costs Pane
        JPanel costPane = new JPanel();
        costPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_COSTS_BORDER)));
        
        constraint.gridx = 3;
        constraint.gridy = 0;
        constraint.gridwidth = 3;
        constraint.gridheight = 4;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.4;
        
        center.add(costPane, constraint);
        
        //Duration Pane
        JPanel durationPane = new JPanel();
        durationPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_DURATION_BORDER)));
        
        constraint.gridx = 3;
        constraint.gridy = 4;
        constraint.gridwidth = 3;
        constraint.gridheight = 3;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.3;
        
        center.add(durationPane, constraint);
        
        //Delivery Delay
        constraint.gridx = 3;
        constraint.gridy = 7;
        constraint.gridwidth = 3;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.1;
        
        center.add(deliveryDelay, constraint);
        
        //Initial Stock
        constraint.gridx = 3;
        constraint.gridy = 8;
        constraint.gridwidth = 3;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 0.1;
        
        center.add(initialStock, constraint);
        
        //Duration Pane
        JPanel supplyChainPane = new JPanel(new BorderLayout());
        supplyChainPane.setBorder(new TitledBorder(Localize.getTextForKey(LocalizationKeys.TITLE_CREATEGAME_SUPPLYCHAIN_BORDER)));
        
        constraint.gridx = 6;
        constraint.gridy = 0;
        constraint.gridwidth = 3;
        constraint.gridheight = 10;
        constraint.fill = GridBagConstraints.BOTH;
        constraint.weightx = 0.4;
        constraint.weighty = 1.0;
        
        center.add(supplyChainPane, constraint);
        
        this.add(center);
    }
    
}
