package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.html.H1;

import org.springframework.security.access.annotation.Secured;

@Secured(ViewNames.HOME_VIEW)
public class HomeView extends AbstractView {

    private static final long serialVersionUID = -2333684897315095897L;

    @Override
    protected void setViewName() {
        this.viewName = ViewNames.HOME_VIEW;

    }

    public HomeView() {
        H1 header = new H1("Welcome");
        this.add(header);
    }
    
}