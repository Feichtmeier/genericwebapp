package org.feichtmeier.genericwebapp.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.apache.commons.lang3.StringUtils;
import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.repository.PermissionRepository;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@Component
@VaadinSessionScope
@Secured(ViewNames.ROLE_VIEW)
public class RoleView extends AbstractView {

    private static final long serialVersionUID = 1L;

    private final Grid<Role> roleGrid;
    private final Dialog roleEditorDialog;
    private final Button newRoleButton;
    private final TextField roleFilter;
    private final HorizontalLayout viewTopLayout;
    private final VerticalLayout viewScrollLayout;
    private Role currentEntity;
    private final VerticalLayout topLayout;
    private final HorizontalLayout bottomLayout;
    private final VerticalLayout dialogBody;
    private final Binder<Role> roleBinder;
    private final Button saveButton, cancelButton, deleteButton;
    private final RoleRepository roleRepository;

    private final TextField name;
    private final Label permissionLabel;
    private final MultiSelectListBox<Permission> permissionListBox;

    private final PermissionRepository permissionRepository;

    private List<Role> roles;

    private List<Permission> permissions;

    public RoleView(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;

        roleEditorDialog = new Dialog();

        viewTopLayout = new HorizontalLayout();
        roleFilter = new TextField("", "Search ...");
        roleFilter.setValueChangeMode(ValueChangeMode.EAGER);
        roleFilter.addValueChangeListener(e -> listEntities(e.getValue()));

        
        roleGrid = new Grid<>(Role.class);
        roleGrid.removeAllColumns();
        roleGrid.addColumn("name");
        refreshRoles();

        newRoleButton = new Button(VaadinIcon.PLUS.create(), e -> {
            editEntity(new Role(""));
        });

        viewTopLayout.add(newRoleButton, roleFilter);

        roleGrid.asSingleSelect().addValueChangeListener(event -> {
            editEntity(event.getValue());
        });

        viewScrollLayout = new VerticalLayout();
        viewScrollLayout.add(roleGrid);

        this.add(viewTopLayout, viewScrollLayout);

        roleBinder = new Binder<>(Role.class);

        saveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (roleBinder.validate().isOk()) {
                // TODO: move to service
                roleRepository.save(currentEntity);
                createNotification("Saved Role " + currentEntity.getName());
                refreshRoles();
                goBackToView();
            } else {
                createNotification("NOT saved Role " + currentEntity.getName());
            }
        });

        cancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });

        deleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            // TODO: move to service
            roleRepository.delete(currentEntity);
            createNotification("Deleted " + currentEntity.getName());
            refreshRoles();
            goBackToView();
        });

        bottomLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);

        topLayout = new VerticalLayout();

        dialogBody = new VerticalLayout(topLayout, bottomLayout);

        roleEditorDialog.add(dialogBody);
        roleEditorDialog.setCloseOnEsc(false);
        roleEditorDialog.setCloseOnOutsideClick(false);

        name = new TextField("");
        name.setLabel("Name of the role");

        permissionListBox = new MultiSelectListBox<>();
        refreshPermissions();
        permissionLabel = new Label("Allowed views");
        topLayout.add(name, permissionLabel, permissionListBox);
        roleBinder.forField(name).asRequired("Must chose a role name").bind(Role::getName, Role::setName);

        permissionListBox.addSelectionListener(e -> {
            currentEntity.setPermissions(e.getValue());
        });

        applyStyling();
    }

    public void editEntity(Role entity) {
        roleEditorDialog.open();
        if (entity == null) {
            roleEditorDialog.close();
            return;
        }

        this.currentEntity = entity;
        roleBinder.setBean(currentEntity);

        permissionListBox.setItems(permissions);
        if (null != currentEntity.getPermissions()) {
            permissionListBox.select(this.currentEntity.getPermissions());
        }
    }

    private void goBackToView() {
        permissionListBox.deselectAll();
        roleEditorDialog.close();
    }

    private void listEntities(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            roleGrid.setItems(roles);
        } else {
            roleGrid.setItems(listRolesByName(filterText, roles));
        }
    }

    private List<Role> listRolesByName(String filterText, List<Role> allowedRoles) {
        if (null == filterText) {
            return allowedRoles;
        } else {
            List<Role> matchedRoles = new ArrayList<>();
            for (Role role : allowedRoles) {
                if (null != role.getName() && role.getName().toUpperCase().startsWith(filterText.toUpperCase())) {
                    matchedRoles.add(role);
                }
            }
            return matchedRoles;
        }
    }

    // TODO: move to service
    private void refreshRoles() {
        roles = roleRepository.findAll();
        roleGrid.setItems(roles);
    }
    // TODO: move to service
    private void refreshPermissions() {
        permissions = permissionRepository.findAll();
        permissionListBox.setItems(permissions);
    }

    public Grid<Role> createGrid() {
        Grid<Role> roleGrid = new Grid<>(Role.class);
        roleGrid.removeAllColumns();
        roleGrid.addColumn("name");
        return roleGrid;
    }

    @Override
    public void applyStyling() {
        setSizeFull();
        roleFilter.setMinWidth("7em");
        roleFilter.getStyle().set("flex-grow", "1");
        roleGrid.setHeightByRows(true);
        viewScrollLayout.setWidthFull();
        viewScrollLayout.setHeight(null);
        viewScrollLayout.getStyle().set("overflow-y", "auto");
        viewScrollLayout.getStyle().set("padding", "0");
        roleGrid.getStyle().set("overflow-y", "auto");
        dialogBody.setHeightFull();
        dialogBody.setPadding(false);
        dialogBody.setMargin(false);
        viewTopLayout.setWidthFull();
        viewTopLayout.getStyle().set("display", "flex");
        viewTopLayout.getStyle().set("flex-direction", "row");
        newRoleButton.getStyle().set("flex-grow", "0");
        newRoleButton.getElement().getThemeList().add("primary");
        setAlignItems(Alignment.CENTER);
        roleEditorDialog.setHeight(null);
        saveButton.getElement().getThemeList().add("primary");
        saveButton.getStyle().set("flex-grow", "1");
        saveButton.getStyle().set("margin-top", "0");
        saveButton.getStyle().set("margin-bottom", "0");
        cancelButton.getStyle().set("flex-grow", "1");
        cancelButton.getStyle().set("margin-top", "0");
        cancelButton.getStyle().set("margin-bottom", "0");
        deleteButton.getElement().getThemeList().add("error");
        deleteButton.getStyle().set("flex-grow", "1");
        deleteButton.getStyle().set("margin-top", "0");
        deleteButton.getStyle().set("margin-bottom", "0");
        bottomLayout.setAlignItems(Alignment.CENTER);
        bottomLayout.setWidthFull();
        bottomLayout.getStyle().set("flex-grow", "0");
        topLayout.setPadding(false);
        topLayout.getStyle().set("flex-grow", "1");
        topLayout.setWidthFull();
        topLayout.setHeight(null);
        topLayout.getStyle().set("overflow-y", "auto");
        topLayout.setPadding(false);
        topLayout.setMargin(false);
        name.setWidthFull();
        permissionLabel.setWidthFull();
        permissionLabel.getStyle().set("margin-top", "10px");
        permissionLabel.getStyle().set("font-size", "0.875rem");
        permissionLabel.getStyle().set("color", "var(--lumo-primary-color)");
        permissionLabel.getStyle().set("font-weight", "bold");
        permissionListBox.setWidthFull();
    }

}
