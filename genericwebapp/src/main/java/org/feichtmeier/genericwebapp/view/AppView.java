package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
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
public class AppView extends AppLayout implements StyledView {

    private static final long serialVersionUID = 8375254280365769233L;

    private final Tabs viewTabs;
    private final Tab homeTab;
    private final Tab userTab;
    private final Tab roleTab;
    private final Tab settingsTab;

    private final Map<Tab, VerticalLayout> tabToViewMap;

    public AppView(RoleView roleView, UserView userView, HomeView homeView, SettingsView settingsView) {

        // Build Notebook based on permissions
        // HomeView and SettingsView are not secured
        tabToViewMap = new HashMap<>();

        homeTab = createTabAndLinkToView(homeView, "Welcome", VaadinIcon.HOME.create());
        userTab = createTabAndLinkToView(userView, "Users", VaadinIcon.USERS.create());
        roleTab = createTabAndLinkToView(roleView, "Roles", VaadinIcon.KEY.create());
        settingsTab = createTabAndLinkToView(settingsView, "Settings", VaadinIcon.COG.create());

        viewTabs = new Tabs(homeTab);
        if (SecurityUtils.isAccessGranted(UserView.class)) {
            viewTabs.add(userTab);
        }
        if (SecurityUtils.isAccessGranted(RoleView.class)) {
            viewTabs.add(roleTab);
        }
        viewTabs.add(settingsTab);
        if (viewTabs.getComponentCount() > 0) {
            click((Tab)viewTabs.getComponentAt(0));
        }

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
    }

    private void click(Tab tab) {
        viewTabs.setSelectedTab(tab);
        final VerticalLayout view = tabToViewMap.get(tab);
        setContent(view);
    }

    private Tab createTabAndLinkToView(VerticalLayout view, String tabText, Icon icon) {
        Label label = new Label(tabText);
        label.setClassName("tab-label");
        FlexLayout tabLayout = new FlexLayout(icon, label);
        tabLayout.setClassName("tab-with-icon");
        icon.setClassName("tab-icon");
        final Tab tab = new Tab(tabLayout);
        
        this.tabToViewMap.put(tab, view);
        
        return tab;
    }

    @Override
    @PostConstruct
    public void linkComponentsToCss() {
        viewTabs.setId("view-tabs");
        this.setId("app-view");
    }
}