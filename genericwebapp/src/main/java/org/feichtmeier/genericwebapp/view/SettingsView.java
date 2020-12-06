package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.springframework.stereotype.Component;

@CssImport("./styles/views/settings-view.css")
@VaadinSessionScope
@Component
public class SettingsView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final Anchor logout = new Anchor("logout", "Logout");
    private final H1 header = new H1("Settings");

    public SettingsView() {
        setId("settingsView");
        logout.setId("logout");
        add(header);
        add(new HorizontalLayout(logout));
    }
}
