package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.entity.View;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.view.util.DarkthemeToggleButton;
import org.feichtmeier.genericwebapp.view.util.Resizeable;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "")
@PreserveOnRefresh
public class AppView extends AppLayout implements Resizeable {

    private static final long serialVersionUID = 8375254280365769233L;

    private final NewUserView userView;
    private final RoleView roleView;

    private final Tabs viewTabs;
    private final Map<Tab, AbstractView> tabToViewMap;

    public AppView(NewUserView adminView, HomeView homeView, GenericRepository<User> genericUserRepository,
            GenericRepository<Role> genericRoleRepository, GenericRepository<Permission> permissionRepository,
            GenericRepository<View> viewRepository, PasswordEncoder passwordEncoder, UserRepository userRepository, RoleRepository roleRepository) {
        // all main "views"
        this.userView = new NewUserView(userRepository, roleRepository, passwordEncoder);
        this.roleView = new RoleView(genericRoleRepository, permissionRepository);

        // Build Notebook based on permissions
        tabToViewMap = new HashMap<>();

        Tab homeTab = createTabAndLinkToView(homeView, "Welcome", VaadinIcon.HOME.create());
        Tab userTab = createTabAndLinkToView(this.userView, "User Administration", VaadinIcon.USER.create());
        Tab roleTab = createTabAndLinkToView(this.roleView, "Role Administration", VaadinIcon.KEY.create());

        boolean userViewAllowed = SecurityUtils.isAccessGranted(UserView.class);
        boolean roleViewAllowed = SecurityUtils.isAccessGranted(RoleView.class);
        boolean homeViewAllowed = SecurityUtils.isAccessGranted(HomeView.class);

        viewTabs = new Tabs();
        if (homeViewAllowed) {
            viewTabs.add(homeTab);
            click(homeTab);
        }
        if (userViewAllowed) {
            viewTabs.add(userTab);
            if (!homeViewAllowed) {
                click(userTab);
            }
        }
        if (roleViewAllowed) {
            viewTabs.add(roleTab);
            if (!homeViewAllowed && !userViewAllowed) {
                click(roleTab);
            }
        }

        viewTabs.setFlexGrowForEnclosedTabs(1);
        viewTabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        viewTabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final AbstractView view = tabToViewMap.get(selectedTab);
            view.refresh();
            setContent(view);
        });
        viewTabs.getStyle().set("flex-grow", "1");
        viewTabs.getStyle().set("margin-right", "35px");

        HorizontalLayout navbarLayout = new HorizontalLayout();
        navbarLayout.getStyle().set("display", "flex");
        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.getStyle().set("flex-grow", "0");
        navbarLayout.add(drawerToggle, viewTabs);
        navbarLayout.setSizeFull();
        addToNavbar(true, navbarLayout);
        VerticalLayout drawerLayout = new VerticalLayout(new DarkthemeToggleButton(), new Anchor("logout", "Log out"));
        addToDrawer(drawerLayout);
        setDrawerOpened(false);

        applyResponsivePadding(navbarLayout, 20, 0);
    }

    private void click(Tab tab) {
        viewTabs.setSelectedTab(tab);
        final AbstractView view = tabToViewMap.get(tab);
        view.refresh();
        setContent(view);
    }

    private Tab createTabAndLinkToView(AbstractView view, String tabText, Icon icon) {
        final Tab tab = new Tab(icon);
        this.tabToViewMap.put(tab, view);

        return tab;
    }

}