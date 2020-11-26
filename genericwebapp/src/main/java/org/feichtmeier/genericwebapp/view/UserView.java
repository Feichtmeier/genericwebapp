package org.feichtmeier.genericwebapp.view;

import java.util.List;

import com.vaadin.flow.component.button.Button;
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
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.feichtmeier.genericwebapp.repository.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Secured(ViewNames.USER_VIEW)
@VaadinSessionScope
public class UserView extends AbstractView implements UserFilter {

    private static final long serialVersionUID = 7368213324544313846L;

    private final Grid<User> userGrid;
    private final Button newUserButton;
    private final TextField userFilter;
    private final HorizontalLayout viewTopLayout;
    private final VerticalLayout viewScrollLayout;
    
    private UserRepository userRepository;

    private User currentUser;
    private final VerticalLayout dialogTopLayout;
    private final HorizontalLayout dialogBottomLayout;
    private final VerticalLayout dialogBody;
    private final Binder<User> userBinder;
    private final Button saveButton, cancelButton, deleteButton;

    private final TextField fullName;
    private final TextField username;
    private final EmailField email;
    private final PasswordField password;
    private final MultiSelectListBox<Role> rolesListBox;
    private final Label rolesLabel;
    private GenericRepository<Role> roleRepository;

    private Dialog userEditorDialog;

    public UserView(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;

        userEditorDialog = new Dialog();

        userFilter = new TextField("", "Search ...");
        userFilter.setValueChangeMode(ValueChangeMode.EAGER);
        userFilter.addValueChangeListener(e -> listEntities(e.getValue()));

        newUserButton = new Button(VaadinIcon.PLUS.create(), e -> {
            editEntity(new User("", ""));
        });

        viewTopLayout = new HorizontalLayout();
        viewTopLayout.add(newUserButton, userFilter);

        userGrid = new Grid<>(User.class);
        userGrid.removeAllColumns();
        userGrid.addColumns("username", "fullName");
        userGrid.setItems(getAllowedEntities());
        userGrid.asSingleSelect().addValueChangeListener(event -> {
            editEntity(event.getValue());
        });
        viewScrollLayout = new VerticalLayout(userGrid);

        add(viewTopLayout, viewScrollLayout);

        userBinder = new Binder<>(User.class);
        this.roleRepository = roleRepository;

        saveButton = new Button("", VaadinIcon.CHECK.create(), e -> {
            if (userBinder.validate().isOk()) {
                userRepository.save(currentUser);
                createNotification("Saved User: " + currentUser.getFullName());

                goBackToView();
            } else {
                createNotification("NOT saved User: " + currentUser.getFullName());
            }
        });

        cancelButton = new Button("", VaadinIcon.CLOSE.create(), e -> {
            goBackToView();
        });

        deleteButton = new Button("", VaadinIcon.TRASH.create(), e -> {
            userRepository.delete(currentUser);
            createNotification("Deleted User: " + currentUser.getFullName());
            refresh();
            goBackToView();
        });

        dialogBottomLayout = new HorizontalLayout(saveButton, cancelButton, deleteButton);
        dialogTopLayout = new VerticalLayout();
        dialogBody = new VerticalLayout(dialogTopLayout, dialogBottomLayout);

        userEditorDialog.add(dialogBody);
        userEditorDialog.setCloseOnEsc(false);
        userEditorDialog.setCloseOnOutsideClick(false);

        fullName = new TextField("");
        username = new TextField("");
        email = new EmailField("");
        password = new PasswordField("");
        rolesLabel = new Label("Roles");
        rolesListBox = new MultiSelectListBox<>();
        rolesListBox.addSelectionListener(e -> {
            currentUser.setRoles(e.getValue());
        });

        userBinder.forField(fullName).asRequired("Must provide a full name")
                .withValidator(new RegexpValidator("Not a valid full name", "(?i)[a-z]+(\\s+[a-z]+)*"))
                .bind("fullName");

        userBinder.forField(username).asRequired("Must provide a username")
                .withValidator(new RegexpValidator("Not a valid username", "^[a-zA-Z0-9]+$")).bind("username");

        userBinder.forField(password).asRequired("Password is not allowed to be empty")
                .withValidator(pass -> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
                        "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(user -> password.getEmptyValue(), (user, pass) -> {
                    if (!password.getEmptyValue().equals(pass)) {
                        user.setPasswordHash(passwordEncoder.encode(pass));
                    }
                });
        userBinder.forField(email).asRequired("Must provide an e-mail address").bind(User::getEmail, User::setEmail);

        dialogTopLayout.add(fullName, username, email, password, rolesLabel, rolesListBox);

        applyStyling();
    }

    private void listEntities(String filterText) {
        if (StringUtils.isEmpty(filterText)) {
            userGrid.setItems(getAllowedEntities());
        } else {
            userGrid.setItems(listUsersByFullName(filterText, getAllowedEntities()));
        }
    }

    @Override
    protected void refresh() {
        userGrid.setItems(getAllowedEntities());
    }

    public List<User> getAllowedEntities() {
        return userRepository.findAll();
    }

    public void editEntity(User entity) {
        userEditorDialog.open();
        if (entity == null) {
            userEditorDialog.close();
            return;
        }

        this.currentUser = entity;
        userBinder.setBean(currentUser);

        rolesListBox.setItems(roleRepository.findAll());
        if (null != currentUser.getRoles()) {
            rolesListBox.select(this.currentUser.getRoles());
        }
    }

    private void createNotification(String text) {
        Notification notification = new Notification();
        notification.setDuration(2000);
        notification.open();
    }

    private void goBackToView() {
        rolesListBox.deselectAll();
        userEditorDialog.close();
        refresh();
    }

    @Override
    public void applyStyling() {
        this.setSizeFull();
        userFilter.setMinWidth("7em");
        userFilter.getStyle().set("flex-grow", "1");
        viewTopLayout.setWidthFull();
        userGrid.setHeightByRows(true);
        viewScrollLayout.setWidthFull();
        viewScrollLayout.setHeight(null);
        viewScrollLayout.getStyle().set("overflow-y", "auto");
        viewScrollLayout.getStyle().set("padding", "0");
        userGrid.getStyle().set("overflow-y", "auto");
        viewTopLayout.getStyle().set("display", "flex");
        viewTopLayout.getStyle().set("flex-direction", "row");
        newUserButton.getStyle().set("flex-grow", "0");
        newUserButton.getElement().getThemeList().add("primary");
        this.setAlignItems(Alignment.CENTER);

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

        fullName.setLabel("Full Name");
        fullName.setWidthFull();
        fullName.getStyle().set("paddin-top", "0");
        fullName.getStyle().set("margin-top", "0");
        fullName.getStyle().set("margin-bottom", "0");

        username.setLabel("Username");
        username.setWidthFull();
        username.getStyle().set("margin-top", "0");
        username.getStyle().set("margin-bottom", "0");

        email.setLabel("E-Mail");
        email.setWidthFull();
        email.getStyle().set("margin-top", "0");
        email.getStyle().set("margin-bottom", "0");

        password.setLabel("Password");
        password.setWidthFull();
        password.getStyle().set("margin-top", "0");
        password.getStyle().set("margin-bottom", "0");

        rolesLabel.setWidthFull();
        rolesLabel.getStyle().set("margin-top", "10px");
        rolesLabel.getStyle().set("font-size", "0.875rem");
        rolesLabel.getStyle().set("color", "var(--lumo-primary-color)");
        rolesLabel.getStyle().set("font-weight", "bold");

        rolesListBox.setWidthFull();

        userEditorDialog.setHeight(null);
    }    
}
