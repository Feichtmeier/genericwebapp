package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.service.UserService;
import org.springframework.stereotype.Component;

@CssImport("./styles/views/home-view.css")
@Component
@VaadinSessionScope
public class HomeView extends AbstractView {

    private static final long serialVersionUID = -2333684897315095897L;

    private final H1 welcomeHeader;
    private final H2 userName;

    public HomeView(UserService userService) {
        userName = new H2("");
        User currentUser = userService.findByUsername(SecurityUtils.getUsername());
        userName.setText(currentUser.getFullName());
        welcomeHeader = new H1("Welcome");
        add(welcomeHeader, userName);
    }

    @Override
    public void linkComponentsToCss() {
        setId("home-view");
    }
}