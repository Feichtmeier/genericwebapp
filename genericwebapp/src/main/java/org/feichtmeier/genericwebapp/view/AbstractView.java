package org.feichtmeier.genericwebapp.view;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public abstract class AbstractView extends VerticalLayout implements StyledView {

    private static final long serialVersionUID = -3072319566313232865L;

    public AbstractView() {
    }

    @PostConstruct
    public abstract void linkComponentsToCss();
}
