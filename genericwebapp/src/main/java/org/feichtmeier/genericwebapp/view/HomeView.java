package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.html.H1;

import org.springframework.security.access.annotation.Secured;

@Secured(ViewNames.HOME_VIEW)
public class HomeView extends AbstractView {

    private static final long serialVersionUID = -2333684897315095897L;

    private final H1 header;

    public HomeView() {
        header = new H1("Welcome");
        this.setAlignItems(Alignment.CENTER);
        this.add(header);
    }

    @Override
    protected void refresh() {
    }
    
}