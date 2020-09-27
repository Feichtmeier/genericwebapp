package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.Map;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Project;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.entity.View;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.ProjectImageRepository;
import org.feichtmeier.genericwebapp.repository.ProjectRepository;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.view.util.DarkthemeToggleButton;
import org.feichtmeier.genericwebapp.view.util.WindowResizer;
import org.springframework.security.crypto.password.PasswordEncoder;

@Route(value = "")
@PreserveOnRefresh
public class AppView extends AppLayout implements WindowResizer {

    private static final long serialVersionUID = 8375254280365769233L;

    public static Project GLOBAL_PROJECT;

    private final UserView userView;
    private final RoleView roleView;
    private final HomeView homeView;
    private final ProjectView projectView;

    private final Tabs viewTabs;
    private final Map<Tab, AbstractView> tabToViewMap;

    private ProjectRepository projectRepository;

    public AppView(GenericRepository<User> genericUserRepository, GenericRepository<Role> roleRepository,
            GenericRepository<Permission> permissionRepository, GenericRepository<View> viewRepository,
            PasswordEncoder passwordEncoder, UserRepository userRepository,
            ProjectRepository projectRepository, ProjectImageRepository projectImageRepository) {
        
        this.projectRepository = projectRepository;
        
        // all main "views"
        this.userView = new UserView(genericUserRepository, roleRepository, projectRepository, passwordEncoder);
        this.roleView = new RoleView(roleRepository, permissionRepository);
        this.homeView = new HomeView();
        this.projectView = new ProjectView(projectRepository, projectImageRepository);

        // Build Notebook based on permissions
        tabToViewMap = new HashMap<>();

        Tab homeTab = createTabAndLinkToView(homeView, "Welcome", VaadinIcon.HOME.create());
        Tab userTab = createTabAndLinkToView(this.userView, "User Administration", VaadinIcon.USER.create());
        Tab roleTab = createTabAndLinkToView(this.roleView, "Role Administration", VaadinIcon.KEY.create());
        Tab projectTab = createTabAndLinkToView(this.projectView, "Projects", VaadinIcon.BUILDING.create());

        boolean userViewAllowed = SecurityUtils.isAccessGranted(UserView.class);
        boolean roleViewAllowed = SecurityUtils.isAccessGranted(RoleView.class);
        boolean homeViewAllowed = SecurityUtils.isAccessGranted(HomeView.class);
        boolean projectViewAllowed = SecurityUtils.isAccessGranted(ProjectView.class);

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
        if (projectViewAllowed) {
            viewTabs.add(projectTab);
            if (!homeViewAllowed && !homeViewAllowed && !userViewAllowed) {
                click(projectTab);
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

        createSelectProjectDialog();
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

    public void createSelectProjectDialog() {
        ComboBox<Project> projectComboBox = new ComboBox<>();
        projectComboBox.setWidthFull();
        projectComboBox.setLabel("Please select a project");
        projectComboBox.setItems(projectRepository.findAll());
        
        VerticalLayout verticalLayout = new VerticalLayout(projectComboBox);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        Dialog projectDialog = new Dialog(verticalLayout);
        projectDialog.setCloseOnEsc(false);
        projectDialog.setCloseOnOutsideClick(false);

        projectComboBox.setPlaceholder("Project ...");
        projectComboBox.addValueChangeListener(e -> {
            projectDialog.close();
            GLOBAL_PROJECT = e.getValue();
        });
        projectDialog.open();
    }

}