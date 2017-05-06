package edu.utfpr.ct.hostgui.utils;

import javafx.event.Event;
import javafx.event.EventHandler;

public abstract class ParameterEventHandler<T extends Event> implements EventHandler<T>{
    protected Object[] params;
    public ParameterEventHandler(Object ... args) {
        params = args;
    }
    
}
