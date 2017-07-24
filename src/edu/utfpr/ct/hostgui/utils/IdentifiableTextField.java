/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import javafx.scene.control.TextField;

/**
 *
 * @author henrique
 */
public class IdentifiableTextField extends TextField{
    private final Integer id;

    public IdentifiableTextField(Integer id) {
        super();
        
        this.id = id;
    }

    public IdentifiableTextField(Integer id, String text) {
        super(text);
        
        this.id = id;
    }

    public Integer getIdentificator() {
        return id;
    }
}
