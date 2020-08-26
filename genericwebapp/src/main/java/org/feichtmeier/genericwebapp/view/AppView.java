package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.entity.View;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "")
public class AppView extends AppLayout {

    private static final long serialVersionUID = 8375254280365769233L;

    private final UserView userView;
    private final RoleView roleView;
    private final Tabs viewTabs;
    private Map<Tab, Component> tabToViewMap;

    public AppView(GenericRepository<User> userRepository, GenericRepository<Role> roleRepository,
            GenericRepository<Permission> permissionRepository, GenericRepository<View> viewRepository,
            PasswordEncoder passwordEncoder) {

        // all main "views"
        this.userView = new UserView(userRepository, roleRepository, passwordEncoder);
        this.roleView = new RoleView(roleRepository, permissionRepository);

        VerticalLayout welcomeView = new VerticalLayout(new H1("Welcome"));

        // Build Notebook based on permissions
        tabToViewMap = new HashMap<>();
        viewTabs = new Tabs(createTabAndLinkToView(welcomeView, "Welcome", VaadinIcon.HOME.create()));
        if (SecurityUtils.isAccessGranted(UserView.class)) {
            viewTabs.add(createTabAndLinkToView(this.userView, "User Administration", VaadinIcon.USER.create()));
        }
        if (SecurityUtils.isAccessGranted(Role.class)) {
            viewTabs.add(createTabAndLinkToView(this.roleView, "Role Administration", VaadinIcon.KEY.create()));
        }

        setContent(welcomeView);
        viewTabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        viewTabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tabToViewMap.get(selectedTab);
            setContent(component);
        });

        // Logo and drawer [ <--[X|X]--> | [ ] ]
        StreamResource res = new StreamResource("logo.svg", () -> AppView.class.getResourceAsStream("/" + "logo.svg"));
        Image img = new Image(res, "Logo");
        img.setHeight("25px");
        HorizontalLayout navbarLeftHalf = new HorizontalLayout();
        navbarLeftHalf.setWidth("170px");
        navbarLeftHalf.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        navbarLeftHalf.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarLeftHalf.add(new DrawerToggle(), img);

        // Items in the navbar right side [ [ ] | <--[X|X|X|X]--> ]
        HorizontalLayout navbarRightHalf = new HorizontalLayout();
        navbarRightHalf.setSizeFull();
        navbarRightHalf.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        navbarRightHalf.setAlignItems(FlexComponent.Alignment.CENTER);
        navbarRightHalf.add(viewTabs);

        // Navbar [<------------->]
        HorizontalLayout navbarLayout = new HorizontalLayout();
        navbarLayout.setSizeFull();
        navbarLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        navbarLayout.add(navbarLeftHalf, navbarRightHalf);
        navbarLayout.setFlexGrow(0, navbarLeftHalf);
        navbarLayout.setFlexGrow(3, navbarRightHalf);

        addToNavbar(true, navbarLayout);
        addToDrawer(new DarkthemeToggleButton(), new Anchor("logout", "Log out"));
        setDrawerOpened(false);
    }

    private Tab createTabAndLinkToView(Component view, String tabText, Icon icon) {
        final Tab tab = new Tab(icon);
        this.tabToViewMap.put(tab, view);
        
        return tab;
    }
}