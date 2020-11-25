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
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.security.SecurityUtils;

@Route(value = "")
@PreserveOnRefresh
@VaadinSessionScope
public class AppView extends AppLayout implements Styleable {

    private static final long serialVersionUID = 8375254280365769233L;

    private final Tabs viewTabs;
    private final HorizontalLayout navbarLayout;
    private final DrawerToggle drawerToggle;

    private final Map<Tab, AbstractView> tabToViewMap;

    public AppView(RoleView roleView, UserView userView, HomeView homeView) {

        // Build Notebook based on permissions
        tabToViewMap = new HashMap<>();

        Tab homeTab = createTabAndLinkToView(homeView, "Welcome", VaadinIcon.HOME.create());
        Tab userTab = createTabAndLinkToView(userView, "User Administration", VaadinIcon.USER.create());
        Tab roleTab = createTabAndLinkToView(roleView, "Role Administration", VaadinIcon.KEY.create());

        boolean userViewAllowed = SecurityUtils.isAccessGranted(UserView.class);
        boolean roleViewAllowed = SecurityUtils.isAccessGranted(RoleView.class);
        boolean homeViewAllowed = SecurityUtils.isAccessGranted(HomeView.class);

        // 1 0 0
        if (homeViewAllowed && !userViewAllowed && !roleViewAllowed) {
            viewTabs = new Tabs(homeTab);
            click(homeTab);
        }
        // 0 1 0
        else if (!homeViewAllowed && userViewAllowed && !roleViewAllowed) {
            viewTabs = new Tabs(userTab);
            click(userTab);
        }
        // 0 0 1
        else if (!homeViewAllowed && !userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(roleTab);
            click(roleTab);
        }
        // 0 1 1
        else if (!homeViewAllowed && userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(userTab, roleTab);
            click(userTab);
        }
        // 1 0 1
        else if (homeViewAllowed && !userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(homeTab, roleTab);
            click(homeTab);
        }
        // 1 1 1
        else if (homeViewAllowed && userViewAllowed && roleViewAllowed) {
            viewTabs = new Tabs(homeTab, userTab, roleTab);
            click(homeTab);
        }
        // 0 0 0
        else {
            viewTabs = new Tabs();
        }

        viewTabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        viewTabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final AbstractView view = tabToViewMap.get(selectedTab);
            view.refresh();
            setContent(view);
        });

        navbarLayout = new HorizontalLayout();
        drawerToggle = new DrawerToggle();
        navbarLayout.add(drawerToggle, viewTabs);

        addToNavbar(true, navbarLayout);
        addToDrawer(new VerticalLayout(new DarkthemeToggleButton(), new Anchor("logout", "Log out")));
        setDrawerOpened(false);

        applyStyling();
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

    @Override
    public void applyStyling() {
        viewTabs.setFlexGrowForEnclosedTabs(1);
        viewTabs.getStyle().set("flex-grow", "1");
        viewTabs.getStyle().set("margin-right", "35px");
        navbarLayout.getStyle().set("display", "flex");
        drawerToggle.getStyle().set("flex-grow", "0");
        navbarLayout.setSizeFull();
    }

}