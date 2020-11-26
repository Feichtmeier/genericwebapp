package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Secured(ViewNames.SETTINGS_VIEW)
@VaadinSessionScope
@Component
public class SettingsView extends AbstractView {

    private static final long serialVersionUID = 1L;

    private final Anchor logout = new Anchor("logout", "Logout");
    private final H1 header = new H1("Settings");

    public SettingsView() {
        add(header);
        add(new HorizontalLayout(logout));
        applyStyling();
    }

    @Override
    protected void refresh() {
    }

    @Override
    public void applyStyling() {
        logout.getStyle().set("color", "red");
        setAlignItems(Alignment.CENTER);
    }

}
