/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.utfpr.ct.hostgui2.utils;

import javafx.event.Event;
import javafx.event.EventHandler;

/**
 *
 * @author henrique
 * @param <T>
 */
public abstract class ParameterEventHandler<T extends Event> implements EventHandler<T>{
    protected Object[] params;
    public ParameterEventHandler(Object ... args) {
        params = args;
    }
    
}
