package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public abstract class AbstractView extends HorizontalLayout {

    private static final long serialVersionUID = -3471469020050609980L;

    public String viewName;

    protected abstract void setViewName();

    public AbstractView() {
        setViewName();
    }
    
}