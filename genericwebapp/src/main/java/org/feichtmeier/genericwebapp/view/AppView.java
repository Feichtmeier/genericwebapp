package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.view.util.JavaScripts;

@CssImport("./styles/views/app-view.css")
@Route(value = "")
@PreserveOnRefresh
@VaadinSessionScope
public class AppView extends AppLayout {

    private static final long serialVersionUID = 8375254280365769233L;

    private final Tabs viewTabs;
    private final Tab homeTab;
    private final Tab userTab;
    private final Tab roleTab;
    private final Tab settingsTab;

    private final Map<Tab, VerticalLayout> tabToViewMap;

    public AppView(RoleView roleView, UserView userView, HomeView homeView, SettingsView settingsView) {

        // Build Notebook based on permissions
        tabToViewMap = new HashMap<>();

        homeTab = createTabAndLinkToView(homeView, "Welcome", VaadinIcon.HOME.create());
        userTab = createTabAndLinkToView(userView, "User Administration", VaadinIcon.USERS.create());
        roleTab = createTabAndLinkToView(roleView, "Role Administration", VaadinIcon.KEY.create());
        settingsTab = createTabAndLinkToView(settingsView, "Settings", VaadinIcon.COG.create());

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
        viewTabs.add(settingsTab);

        viewTabs.setOrientation(Tabs.Orientation.HORIZONTAL);
        viewTabs.addSelectedChangeListener(event -> {
            final Tab selectedTab = event.getSelectedTab();
            final Tab previousTab = event.getPreviousTab();
            final VerticalLayout view = tabToViewMap.get(selectedTab);
            if (!selectedTab.equals(settingsTab)) {
                setContent(view);
            } else {
                final Dialog settingsDialog = new Dialog(settingsView);
                settingsDialog.addDialogCloseActionListener(e -> {
                    click(previousTab);
                    settingsDialog.close();
                });
                settingsDialog.open();
            }
            
        });

        addToNavbar(true, viewTabs);

        getElement().executeJs(JavaScripts.USE_SYSTEM_THEME_SCRIPT);
        setDrawerOpened(false);

        viewTabs.setId("viewTabs");
    }

    private void click(Tab tab) {
        viewTabs.setSelectedTab(tab);
        final VerticalLayout view = tabToViewMap.get(tab);
        setContent(view);
    }

    private Tab createTabAndLinkToView(VerticalLayout view, String tabText, Icon icon) {
        final Tab tab = new Tab(icon);
        this.tabToViewMap.put(tab, view);

        return tab;
    }
}