package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
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
    private final HomeView homeView;

    private Tabs viewTabs;
    private Map<Tab, Component> tabToViewMap;

    public AppView(GenericRepository<User> userRepository, GenericRepository<Role> roleRepository,
            GenericRepository<Permission> permissionRepository, GenericRepository<View> viewRepository,
            PasswordEncoder passwordEncoder) {

        // all main "views"
        this.userView = new UserView(userRepository, roleRepository, passwordEncoder);
        this.roleView = new RoleView(roleRepository, permissionRepository);
        this.homeView = new HomeView();

        // Build Notebook based on permissions
        tabToViewMap = new HashMap<>();

        Tab homeTab = createTabAndLinkToView(homeView, "Welcome", VaadinIcon.HOME.create());
        Tab userTab = createTabAndLinkToView(this.userView, "User Administration", VaadinIcon.USER.create());
        Tab roleTab = createTabAndLinkToView(this.roleView, "Role Administration", VaadinIcon.KEY.create());

        boolean userViewAllowed = SecurityUtils.isAccessGranted(UserView.class);
        boolean roleViewAllowed = SecurityUtils.isAccessGranted(RoleView.class);
        boolean homeViewAllowed = SecurityUtils.isAccessGranted(HomeView.class);

        // 0 | 0 | 0
        if (!homeViewAllowed && !userViewAllowed && !roleViewAllowed) {
            viewTabs = new Tabs();
        }
        // 0 | 0 | 1
        else if (!homeViewAllowed && !userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(roleTab);
            click(roleTab);
        }
        // 0 | 1 | 0
        else if (!homeViewAllowed && userViewAllowed && !roleViewAllowed) {
            viewTabs = new Tabs(userTab);
            click(userTab);
        }
        // 0 | 1 | 1
        else if (!homeViewAllowed && userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(userTab, roleTab);
            click(userTab);
        }
        // 1 | 0 | 0
        else if (homeViewAllowed && !userViewAllowed && !roleViewAllowed) {
            viewTabs = new Tabs(homeTab);
            click(homeTab);
        }
        // 1 | 0 | 1
        else if (homeViewAllowed && !userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(homeTab, roleTab);
            click(homeTab);
        }
        // 1 | 1 | 0
        else if (homeViewAllowed && userViewAllowed && !roleViewAllowed) {
            viewTabs = new Tabs(homeTab, userTab);
            click(homeTab);
        }
        // 1 | 1 | 1
        else if (homeViewAllowed && userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(homeTab, userTab, roleTab);
            click(homeTab);
        }

        viewTabs.setFlexGrowForEnclosedTabs(1);
        viewTabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        viewTabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Component component = tabToViewMap.get(selectedTab);
            setContent(component);
        });

        StreamResource res = new StreamResource("logo.svg", () -> AppView.class.getResourceAsStream("/" + "logo.svg"));
        Image img = new Image(res, "Logo");
        img.setHeight("25px");

        HorizontalLayout navbarLayout = new HorizontalLayout();
        navbarLayout.getStyle().set("display", "flex");
        viewTabs.getStyle().set("flex-grow", "1");
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().set("flex-grow", "0");
        navbarLayout.add(drawerToggle, viewTabs);
        navbarLayout.setSizeFull();
        addToNavbar(true, navbarLayout);
        addToDrawer(new VerticalLayout(new DarkthemeToggleButton(), new Anchor("logout", "Log out")));
        setDrawerOpened(false);
    }

    private void click(Tab tab) {
        viewTabs.setSelectedTab(tab);
        setContent(tabToViewMap.get(tab));
    }

    private Tab createTabAndLinkToView(Component view, String tabText, Icon icon) {
        final Tab tab = new Tab(icon);
        this.tabToViewMap.put(tab, view);

        return tab;
    }
}