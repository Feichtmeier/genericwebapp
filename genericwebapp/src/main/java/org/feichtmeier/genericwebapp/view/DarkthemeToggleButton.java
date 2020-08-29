package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.theme.lumo.Lumo;

public class DarkthemeToggleButton extends Button {
    
    private static final long serialVersionUID = 3958815276510598738L;

    public DarkthemeToggleButton() {
        super(new Icon(VaadinIcon.MOON));
        this.getStyle().set("border-radius", "100%");
        this.getStyle().set("background-color", "transparent");
        this.getStyle().set("padding-left", "5px");
        this.getStyle().set("padding-right", "5px");

        this.addClickListener(event -> {
            ThemeList themeList = UI.getCurrent().getElement().getThemeList();

            if (themeList.contains(Lumo.DARK)) {
                themeList.remove(Lumo.DARK);
                themeList.add(Lumo.LIGHT);
                setIcon(new Icon(VaadinIcon.MOON));
            } else {
                themeList.add(Lumo.DARK);
                themeList.remove(Lumo.LIGHT);
                setIcon(new Icon(VaadinIcon.SUN_O));
            }
        });
    }
}