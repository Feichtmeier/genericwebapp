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
import org.feichtmeier.genericwebapp.service.RoleService;
import org.feichtmeier.genericwebapp.service.UserService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

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
    private Dialog userEditorDialog;
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
    private UserService userService;
    private RoleService roleService;
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
        viewScrollLayout = new VerticalLayout(viewUserGrid);
        // Add to view
        add(viewTopLayout, viewScrollLayout);
        // Dialog
        userEditorDialog = new Dialog();
        // Dialog Top
        dialogFullNameTextField = new TextField("");
        dialogUsernameTextField = new TextField("");
        dialogEmailTextField = new EmailField("");
        dialogPasswordTextField = new PasswordField("");
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
                createNotification("Saved User: " + currentUser.getFullName());

                goBackToView();
            } else {
                createNotification("NOT saved User: " + currentUser.getFullName());
            }
        });
        dialogCancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });
        dialogDeleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            userService.delete(currentUser);
            createNotification("Deleted User: " + currentUser.getFullName());

            goBackToView();
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

        applyStyling();
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

    @Override
    public void applyStyling() {
        this.setSizeFull();
        viewUserFilter.setMinWidth("7em");
        viewUserFilter.getStyle().set("flex-grow", "1");
        viewTopLayout.setWidthFull();
        viewUserGrid.setHeightByRows(true);
        viewScrollLayout.setWidthFull();
        viewScrollLayout.setHeight(null);
        viewScrollLayout.getStyle().set("overflow-y", "auto");
        viewScrollLayout.getStyle().set("padding", "0");
        viewUserGrid.getStyle().set("overflow-y", "auto");
        viewTopLayout.getStyle().set("display", "flex");
        viewTopLayout.getStyle().set("flex-direction", "row");
        viewNewUserButton.getStyle().set("flex-grow", "0");
        viewNewUserButton.getElement().getThemeList().add("primary");
        this.setAlignItems(Alignment.CENTER);

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

        dialogBody.setHeightFull();
        dialogBody.setPadding(false);
        dialogBody.setMargin(false);

        dialogFullNameTextField.setLabel("Full Name");
        dialogFullNameTextField.setWidthFull();
        dialogFullNameTextField.getStyle().set("paddin-top", "0");
        dialogFullNameTextField.getStyle().set("margin-top", "0");
        dialogFullNameTextField.getStyle().set("margin-bottom", "0");

        dialogUsernameTextField.setLabel("Username");
        dialogUsernameTextField.setWidthFull();
        dialogUsernameTextField.getStyle().set("margin-top", "0");
        dialogUsernameTextField.getStyle().set("margin-bottom", "0");

        dialogEmailTextField.setLabel("E-Mail");
        dialogEmailTextField.setWidthFull();
        dialogEmailTextField.getStyle().set("margin-top", "0");
        dialogEmailTextField.getStyle().set("margin-bottom", "0");

        dialogPasswordTextField.setLabel("Password");
        dialogPasswordTextField.setWidthFull();
        dialogPasswordTextField.getStyle().set("margin-top", "0");
        dialogPasswordTextField.getStyle().set("margin-bottom", "0");

        dialogRolesLabel.setWidthFull();
        dialogRolesLabel.getStyle().set("margin-top", "10px");
        dialogRolesLabel.getStyle().set("font-size", "0.875rem");
        dialogRolesLabel.getStyle().set("color", "var(--lumo-primary-color)");
        dialogRolesLabel.getStyle().set("font-weight", "bold");

        dialogRolesListBox.setWidthFull();

        userEditorDialog.setHeight(null);
    }
}
