package edu.utfpr.ct.hostgui.utils;

import javafx.beans.value.ChangeListener;

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
