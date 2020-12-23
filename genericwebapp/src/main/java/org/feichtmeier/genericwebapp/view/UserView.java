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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

import org.apache.commons.lang3.StringUtils;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.security.SecurityUtils;
import org.feichtmeier.genericwebapp.service.RoleService;
import org.feichtmeier.genericwebapp.service.UserService;
import org.feichtmeier.genericwebapp.view.constants.ViewNames;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@CssImport("./styles/views/user-view.css")
@Component
@Secured(ViewNames.USER_VIEW)
@VaadinSessionScope
public class UserView extends AbstractView {

    private static final long serialVersionUID = 7368213324544313846L;
    // UI Fields
    private final Grid<User> viewUserGrid;
    private final Button viewNewUserButton;
    private final TextField viewUserFilter;
    private final HorizontalLayout viewTopLayout;
    private final VerticalLayout viewScrollLayout;
    private final Dialog userEditorDialog;
    private final VerticalLayout dialogTopLayout;
    private final HorizontalLayout dialogBottomLayout;
    private final VerticalLayout dialogBody;
    private final Binder<User> userBinder;
    private final Button dialogSaveButton, dialogCancelButton, dialogDeleteButton;
    private final TextField dialogFullNameTextField;
    private final TextField dialogUsernameTextField;
    private final EmailField dialogEmailTextField;
    private final PasswordField dialogPasswordTextField;
    private final MultiSelectListBox<Role> dialogRolesListBox;
    private final Label dialogRolesLabel;
    // Data Fields
    private final UserService userService;
    private final RoleService roleService;
    private List<User> users;
    private List<Role> roles;
    private User currentUser;

    public UserView(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        // Data
        this.userService = userService;
        this.roleService = roleService;
        userBinder = new Binder<>(User.class);
        // View Top
        viewUserFilter = new TextField("", "Search ...");
        viewUserFilter.setValueChangeMode(ValueChangeMode.EAGER);
        viewUserFilter.addValueChangeListener(e -> listEntities(e.getValue()));
        viewNewUserButton = new Button(VaadinIcon.PLUS.create(), e -> {
            editEntity(new User("", ""));
        });
        viewTopLayout = new HorizontalLayout();
        viewTopLayout.add(viewNewUserButton, viewUserFilter);
        // View Bottom
        viewUserGrid = new Grid<>(User.class);
        viewUserGrid.removeAllColumns();
        viewUserGrid.addColumns("username", "fullName");
        refreshUsers();
        viewUserGrid.asSingleSelect().addValueChangeListener(event -> {
            editEntity(event.getValue());
        });
        viewUserGrid.setHeightByRows(true);
        viewScrollLayout = new VerticalLayout(viewUserGrid);
        // Add to view
        add(viewTopLayout, viewScrollLayout);
        // Dialog
        userEditorDialog = new Dialog();
        // Dialog Top
        dialogFullNameTextField = new TextField("Full Name");
        dialogUsernameTextField = new TextField("Username");
        dialogEmailTextField = new EmailField("E-Mail");
        dialogPasswordTextField = new PasswordField("Password");
        dialogRolesLabel = new Label("Roles");
        dialogRolesListBox = new MultiSelectListBox<>();
        dialogRolesListBox.addSelectionListener(e -> {
            currentUser.setRoles(e.getValue());
        });
        dialogTopLayout = new VerticalLayout();
        dialogTopLayout.add(dialogFullNameTextField, dialogUsernameTextField, dialogEmailTextField,
                dialogPasswordTextField, dialogRolesLabel, dialogRolesListBox);
        // Dialog Bottom
        dialogSaveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (userBinder.validate().isOk()) {
                userService.save(currentUser);
                Notification.show("Saved User: " + currentUser.getFullName());
                goBackToView();
            } else {
                Notification.show("NOT saved User: " + currentUser.getFullName());
            }
        });
        dialogCancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });
        dialogDeleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            if (!SecurityUtils.getUsername().equals(currentUser.getUsername())) {
                userService.delete(currentUser);
                Notification.show("Deleted User: " + currentUser.getFullName());
                goBackToView();
            } else {
                Notification.show("You can't delete your own user here, " + currentUser.getFullName());
            }
        });
        dialogBottomLayout = new HorizontalLayout(dialogSaveButton, dialogCancelButton, dialogDeleteButton);
        // Add to dialog
        dialogBody = new VerticalLayout(dialogTopLayout, dialogBottomLayout);
        userEditorDialog.add(dialogBody);
        userEditorDialog.setCloseOnEsc(false);
        userEditorDialog.setCloseOnOutsideClick(false);
        // bind Data
        userBinder.forField(dialogFullNameTextField).asRequired("Must provide a full name")
                .withValidator(new RegexpValidator("Not a valid full name", "(?i)[a-z]+(\\s+[a-z]+)*"))
                .bind("fullName");

        userBinder.forField(dialogUsernameTextField).asRequired("Must provide a username")
                .withValidator(new RegexpValidator("Not a valid username", "^[a-zA-Z0-9]+$")).bind("username");

        userBinder.forField(dialogPasswordTextField).asRequired("Password is not allowed to be empty")
                .withValidator(pass -> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
                        "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(user -> dialogPasswordTextField.getEmptyValue(), (user, pass) -> {
                    if (!dialogPasswordTextField.getEmptyValue().equals(pass)) {
                        user.setPasswordHash(passwordEncoder.encode(pass));
                    }
                });
        userBinder.forField(dialogEmailTextField).asRequired("Must provide an e-mail address").bind(User::getEmail,
                User::setEmail);
    }

    private void listEntities(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            viewUserGrid.setItems(users);
        } else {
            viewUserGrid.setItems(listUsersByFullName(filterText, users));
        }
    }

    private List<User> listUsersByFullName(String filterText, List<User> allowedUsers) {
        if (null == filterText) {
            return allowedUsers;
        } else {
            List<User> matchedUsers = new ArrayList<>();
            for (User user : allowedUsers) {
                if (null != user.getFullName()
                        && user.getFullName().toUpperCase().startsWith(filterText.toUpperCase())) {
                    matchedUsers.add(user);
                }
            }
            return matchedUsers;
        }

    }

    private void refreshUsers() {
        users = userService.findAll();
        viewUserGrid.setItems(users);
    }

    private void refreshRoles() {
        roles = roleService.findAll();
        dialogRolesListBox.setItems(roles);
    }

    public void editEntity(User entity) {
        if (entity == null) {
            goBackToView();
            return;
        }
        userEditorDialog.open();

        this.currentUser = entity;
        userBinder.setBean(currentUser);

        refreshRoles();
        if (null != currentUser.getRoles()) {
            dialogRolesListBox.select(this.currentUser.getRoles());
        }
    }

    private void goBackToView() {
        dialogRolesListBox.deselectAll();
        viewUserGrid.deselectAll();
        userEditorDialog.close();
        refreshUsers();
        refreshRoles();
        viewUserFilter.clear();
    }

    public void linkComponentsToCss() {
        setId("user-view");
        addClassName("grid-view");
        viewTopLayout.addClassName("grid-view-top-layout");
        viewUserFilter.addClassName("grid-view-filter");
        viewNewUserButton.getElement().getThemeList().add("primary");
        viewNewUserButton.addClassName("grid-view-add-entity-button");
        viewScrollLayout.addClassName("grid-view-scroll-layout");
        viewUserGrid.addClassName("grid-view-entity-grid");
        dialogBody.addClassName("grid-view-editor-dialog-body");
        dialogTopLayout.addClassName("grid-view-editor-dialog-top-layout");
        dialogBottomLayout.addClassName("grid-view-editor-dialog-bottom-layout");
        dialogSaveButton.getElement().getThemeList().add("primary");
        dialogSaveButton.addClassName("grid-view-editor-save-button");
        dialogCancelButton.addClassName("grid-view-editor-cancel-button");
        dialogDeleteButton.getElement().getThemeList().add("error");
        dialogDeleteButton.addClassName("grid-view-editor-delete-button");
        dialogFullNameTextField.setId("user-editor-fullname-textfield");
        dialogUsernameTextField.setId("user-editor-username-textfield");
        dialogPasswordTextField.setId("user-editor-password-textfield");
        dialogEmailTextField.setId("user-editor-email-textfield");
    }
}
