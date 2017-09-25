package edu.utfpr.ct.hostgui.utils;

import javafx.scene.control.TextField;

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
