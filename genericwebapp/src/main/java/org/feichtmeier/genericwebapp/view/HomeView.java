package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.view.constants.ViewNames;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@CssImport("./styles/views/home-view.css")
@Component
@Secured(ViewNames.HOME_VIEW)
@VaadinSessionScope
public class HomeView extends AbstractView {

    private static final long serialVersionUID = -2333684897315095897L;

    private final H1 welcomeHeader;
    private final H2 userName;

    public HomeView(UserRepository userRepository) {
        userName = new H2("");
        User currentUser = userRepository.findByUsername(SecurityUtils.getUsername());
        userName.setText(currentUser.getFullName());
        welcomeHeader = new H1("Welcome");
        add(welcomeHeader, userName);
    }

    @Override
    public void linkComponentsToCss() {
        setId("home-view");
    }
}