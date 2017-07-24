/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui.utils;

import javafx.beans.value.ChangeListener;

/**
 *
 * @author henrique
 */
public abstract class IdentifiableChangeListener<T> implements ChangeListener<T>{
    private final Integer id;

    public IdentifiableChangeListener(Integer id) {
        super();
        
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}
