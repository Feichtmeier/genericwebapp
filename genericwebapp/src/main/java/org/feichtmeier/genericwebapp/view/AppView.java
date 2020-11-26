package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.view.util.JavaScripts;

@Route(value = "")
@PreserveOnRefresh
@VaadinSessionScope
public class AppView extends AppLayout implements Styleable {

    private static final long serialVersionUID = 8375254280365769233L;

    private final Tabs viewTabs;
    private final Tab homeTab;
    private final Tab userTab;
    private final Tab roleTab;
    private final Tab settingsTab;

    private final Map<Tab, AbstractView> tabToViewMap;

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
            final AbstractView view = tabToViewMap.get(selectedTab);
            view.refresh();
            if (!selectedTab.equals(settingsTab)) {
                setContent(view);
            } else {
                Dialog settingsDialog = new Dialog(settingsView);
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
        viewTabs.setWidthFull();
    }

}