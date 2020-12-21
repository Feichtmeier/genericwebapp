package org.feichtmeier.genericwebapp.view;

import java.util.ArrayList;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.apache.commons.lang3.StringUtils;
import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.service.PermissionService;
import org.feichtmeier.genericwebapp.service.RoleService;
import org.feichtmeier.genericwebapp.view.constants.ViewNames;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@CssImport("./styles/views/role-view.css")
@Component
@VaadinSessionScope
@Secured(ViewNames.ROLE_VIEW)
public class RoleView extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    // UI Fields
    private final Grid<Role> viewRoleGrid;
    private final Dialog roleEditorDialog;
    private final Button viewNewRoleButton;
    private final TextField viewRoleFilter;
    private final HorizontalLayout viewTopLayout;
    private final VerticalLayout viewScrollLayout;
    private final VerticalLayout dialogTopLayout;
    private final HorizontalLayout dialogBottomLayout;
    private final VerticalLayout dialogBody;
    private final Binder<Role> roleBinder;
    private final Button dialogSaveButton, dialogCancelButton, dialogDeleteButton;
    private final TextField dialogNameTextField;
    private final Label dialogPermissionLabel;
    private final MultiSelectListBox<Permission> dialogPermissionListBox;
    // Data Fields
    private final RoleService roleService;
    private final PermissionService permissionService;
    private List<Role> roles;
    private List<Permission> permissions;
    private Role currentEntity;

    public RoleView(RoleService roleService, PermissionService permissionService) {
        // Data
        this.permissionService = permissionService;
        this.roleService = roleService;
        roleBinder = new Binder<>(Role.class);
        // View Top
        viewTopLayout = new HorizontalLayout();
        viewRoleFilter = new TextField("", "Search ...");
        viewRoleFilter.setValueChangeMode(ValueChangeMode.EAGER);
        viewRoleFilter.addValueChangeListener(e -> listEntities(e.getValue()));
        viewNewRoleButton = new Button(VaadinIcon.PLUS.create(), e -> {
            editEntity(new Role(""));
        });
        viewNewRoleButton.getElement().getThemeList().add("primary");
        viewTopLayout.add(viewNewRoleButton, viewRoleFilter);
        // View Bottom
        viewRoleGrid = new Grid<>(Role.class);
        viewRoleGrid.removeAllColumns();
        viewRoleGrid.addColumn("name");
        refreshRoles();
        viewRoleGrid.asSingleSelect().addValueChangeListener(event -> {
            editEntity(event.getValue());
        });
        viewRoleGrid.setHeightByRows(true);
        viewScrollLayout = new VerticalLayout();
        viewScrollLayout.add(viewRoleGrid);
        // Add to view
        this.add(viewTopLayout, viewScrollLayout);
        // Edit Dialog
        roleEditorDialog = new Dialog();
        // Dialog top
        dialogTopLayout = new VerticalLayout();
        dialogNameTextField = new TextField("Role name");
        dialogPermissionListBox = new MultiSelectListBox<>();
        dialogPermissionLabel = new Label("Allowed views");
        dialogTopLayout.add(dialogNameTextField, dialogPermissionLabel, dialogPermissionListBox);
        // Dialog bottom
        dialogSaveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (roleBinder.validate().isOk()) {
                roleService.save(currentEntity);
                Notification.show("Saved Role " + currentEntity.getName());
                goBackToView();
            } else {
                Notification.show("NOT saved Role " + currentEntity.getName());
            }
        });
        dialogCancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });
        dialogDeleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            roleService.delete(currentEntity);
            Notification.show("Deleted " + currentEntity.getName());
            goBackToView();
        });
        dialogBottomLayout = new HorizontalLayout(dialogSaveButton, dialogCancelButton, dialogDeleteButton);
        // Add to dialog
        dialogBody = new VerticalLayout(dialogTopLayout, dialogBottomLayout);
        roleEditorDialog.add(dialogBody);
        roleEditorDialog.setCloseOnEsc(false);
        roleEditorDialog.setCloseOnOutsideClick(false);
        // Bind data in dialog
        roleBinder.forField(dialogNameTextField).asRequired("Must chose a role name").bind(Role::getName,
                Role::setName);
        dialogPermissionListBox.addSelectionListener(e -> {
            currentEntity.setPermissions(e.getValue());
        });

        linkComponentsToCss();
    }

    public void editEntity(Role entity) {
        if (entity == null) {
            roleEditorDialog.close();
            return;
        }
        roleEditorDialog.open();
        this.currentEntity = entity;
        roleBinder.setBean(currentEntity);

        refreshPermissions();
        if (null != currentEntity.getPermissions()) {
            dialogPermissionListBox.select(this.currentEntity.getPermissions());
        }
    }

    private void goBackToView() {
        dialogPermissionListBox.deselectAll();
        roleEditorDialog.close();
        refreshRoles();
        refreshPermissions();
        viewRoleFilter.clear();
    }

    private void listEntities(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            viewRoleGrid.setItems(roles);
        } else {
            viewRoleGrid.setItems(listRolesByName(filterText, roles));
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

    private void refreshRoles() {
        roles = roleService.findAll();
        viewRoleGrid.setItems(roles);
    }

    private void refreshPermissions() {
        permissions = permissionService.findAll();
        dialogPermissionListBox.setItems(permissions);
    }

    public void linkComponentsToCss() {
        setId("role-view");
        addClassName("grid-view");
        viewTopLayout.addClassName("grid-view-top-layout");
        viewRoleFilter.addClassName("grid-view-filter");
        viewNewRoleButton.addClassName("grid-view-add-entity-button");
        viewScrollLayout.addClassName("grid-view-scroll-layout");
        viewRoleGrid.addClassName("grid-view-entity-grid");
        dialogBody.addClassName("grid-view-editor-dialog-body");
        dialogTopLayout.addClassName("grid-view-editor-dialog-top-layout");
        dialogBottomLayout.addClassName("grid-view-editor-dialog-bottom-layout");
        dialogSaveButton.getElement().getThemeList().add("primary");
        dialogSaveButton.addClassName("grid-view-editor-save-button");
        dialogCancelButton.addClassName("grid-view-editor-cancel-button");
        dialogDeleteButton.getElement().getThemeList().add("error");
        dialogDeleteButton.addClassName("grid-view-editor-delete-button");
        dialogNameTextField.setId("role-editor-role-name-textfield");
        dialogPermissionLabel.setId("role-editor-permission-label");
        dialogPermissionListBox.setId("role-editor-permission-listbox");
    }
}
