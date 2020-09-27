package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.html.Label;

public class DefaultSmallLabel extends Label {

    private static final long serialVersionUID = 1L;

    public DefaultSmallLabel(String text) {
        super(text);
        setWidthFull();
        getStyle().set("margin-top", "10px");
        getStyle().set("font-size", "0.875rem");
        getStyle().set("color", "var(--lumo-primary-color)");
        getStyle().set("font-weight", "bold");
    }
    
}
