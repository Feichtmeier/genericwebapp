package org.feichtmeier.genericwebapp.view.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;

public class DarkthemeToggleButton extends Button {
    
    private static final long serialVersionUID = 3958815276510598738L;

    public DarkthemeToggleButton() {
        super(new Icon(VaadinIcon.MOON));

        setText("Dark/Light Toggle");

        this.addClickListener(event -> {
            getElement().executeJs("document.querySelector('html').hasAttribute('theme') && document.querySelector('html').getAttribute('theme').includes('dark') ? document.querySelector('html').setAttribute('theme',document.querySelector('html').getAttribute('theme').replaceAll('dark','')) : document.querySelector('html').setAttribute('theme', 'dark');");
        });
    }
}