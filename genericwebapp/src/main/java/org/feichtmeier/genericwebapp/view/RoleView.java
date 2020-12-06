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
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Component;

@CssImport("./styles/views/role-view.css")
@Component
@VaadinSessionScope
@Secured(ViewNames.ROLE_VIEW)
public class RoleView extends VerticalLayout implements Styleable {

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
    private Role currentEntity;
    private final RoleService roleService;
    private final PermissionService permissionService;
    private List<Role> roles;
    private List<Permission> permissions;

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
        viewTopLayout.add(viewNewRoleButton, viewRoleFilter);
        // View Bottom
        viewRoleGrid = new Grid<>(Role.class);
        viewRoleGrid.removeAllColumns();
        viewRoleGrid.addColumn("name");
        refreshRoles();
        viewRoleGrid.asSingleSelect().addValueChangeListener(event -> {
            editEntity(event.getValue());
        });
        viewScrollLayout = new VerticalLayout();
        viewScrollLayout.add(viewRoleGrid);
        // Add to view
        this.add(viewTopLayout, viewScrollLayout);
        // Edit Dialog
        roleEditorDialog = new Dialog();
        // Dialog top
        dialogTopLayout = new VerticalLayout();
        dialogNameTextField = new TextField("");
        dialogNameTextField.setLabel("Name of the role");
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

        applyStyling();
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

    @Override
    public void applyStyling() {
        setSizeFull();
        viewRoleFilter.setMinWidth("7em");
        viewRoleFilter.getStyle().set("flex-grow", "1");
        viewRoleGrid.setHeightByRows(true);
        viewScrollLayout.setWidthFull();
        viewScrollLayout.setHeight(null);
        viewScrollLayout.getStyle().set("overflow-y", "auto");
        viewScrollLayout.getStyle().set("padding", "0");
        viewRoleGrid.getStyle().set("overflow-y", "auto");
        dialogBody.setHeightFull();
        dialogBody.setPadding(false);
        dialogBody.setMargin(false);
        viewTopLayout.setWidthFull();
        viewTopLayout.getStyle().set("display", "flex");
        viewTopLayout.getStyle().set("flex-direction", "row");
        viewNewRoleButton.getStyle().set("flex-grow", "0");
        viewNewRoleButton.getElement().getThemeList().add("primary");
        setAlignItems(Alignment.CENTER);
        roleEditorDialog.setHeight(null);
        dialogSaveButton.getElement().getThemeList().add("primary");
        dialogSaveButton.getStyle().set("flex-grow", "1");
        dialogSaveButton.getStyle().set("margin-top", "0");
        dialogSaveButton.getStyle().set("margin-bottom", "0");
        dialogCancelButton.getStyle().set("flex-grow", "1");
        dialogCancelButton.getStyle().set("margin-top", "0");
        dialogCancelButton.getStyle().set("margin-bottom", "0");
        dialogDeleteButton.getElement().getThemeList().add("error");
        dialogDeleteButton.getStyle().set("flex-grow", "1");
        dialogDeleteButton.getStyle().set("margin-top", "0");
        dialogDeleteButton.getStyle().set("margin-bottom", "0");
        dialogBottomLayout.setAlignItems(Alignment.CENTER);
        dialogBottomLayout.setWidthFull();
        dialogBottomLayout.getStyle().set("flex-grow", "0");
        dialogTopLayout.setPadding(false);
        dialogTopLayout.getStyle().set("flex-grow", "1");
        dialogTopLayout.setWidthFull();
        dialogTopLayout.setHeight(null);
        dialogTopLayout.getStyle().set("overflow-y", "auto");
        dialogTopLayout.setPadding(false);
        dialogTopLayout.setMargin(false);
        dialogNameTextField.setWidthFull();
        dialogPermissionLabel.setWidthFull();
        dialogPermissionLabel.getStyle().set("margin-top", "10px");
        dialogPermissionLabel.getStyle().set("font-size", "0.875rem");
        dialogPermissionLabel.getStyle().set("color", "var(--lumo-primary-color)");
        dialogPermissionLabel.getStyle().set("font-weight", "bold");
        dialogPermissionListBox.setWidthFull();
    }

}
