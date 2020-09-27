package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;

import org.feichtmeier.genericwebapp.entity.Project;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.ProjectRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserEditor extends GenericEntityEditor<User> {

    PasswordEncoder passwordEncoder;

    private static final long serialVersionUID = -7224805495412375614L;

    private final TextField fullName;
    private final TextField username;
    private final EmailField email;
    private final PasswordField password;
    private final MultiSelectListBox<Role> rolesListBox;
    private final MultiSelectListBox<Project> projectListBox;
    private final DefaultSmallLabel projectsLabel;
    private final DefaultSmallLabel rolesLabel;
    private GenericRepository<Role> roleRepository;
    private ProjectRepository projectRepository;

    public UserEditor(GenericRepository<User> userRepository, GenericGridView<User> view) {
        super(userRepository, view);

        fullName = new TextField("");
        fullName.setLabel("Full Name");
        fullName.setWidthFull();
        fullName.getStyle().set("paddin-top", "0");
        fullName.getStyle().set("margin-top", "0");
        fullName.getStyle().set("margin-bottom", "0");

        username = new TextField("");
        username.setLabel("Username");
        username.setWidthFull();
        username.getStyle().set("margin-top", "0");
        username.getStyle().set("margin-bottom", "0");

        email = new EmailField("");
        email.setLabel("E-Mail");
        email.setWidthFull();
        email.getStyle().set("margin-top", "0");
        email.getStyle().set("margin-bottom", "0");

        password = new PasswordField("");
        password.setLabel("Password");
        password.setWidthFull();
        password.getStyle().set("margin-top", "0");
        password.getStyle().set("margin-bottom", "0");

        rolesLabel = new DefaultSmallLabel("Roles");
        rolesLabel.setWidthFull();
        rolesListBox = new MultiSelectListBox<>();
        rolesListBox.addSelectionListener(e -> {
            currentEntity.setRoles(e.getValue());
        });
        rolesListBox.setWidthFull();

        projectsLabel = new DefaultSmallLabel("Projects");
        projectsLabel.setWidthFull();
        projectListBox = new MultiSelectListBox<>();
        projectListBox.addSelectionListener(e -> {
            currentEntity.setProjects(e.getValue());
        });
        projectListBox.setWidthFull();

        binder.forField(fullName).asRequired("Must provide a full name")
                .withValidator(new RegexpValidator("Not a valid full name", "(?i)[a-z]+(\\s+[a-z]+)*"))
                .bind("fullName");

        binder.forField(username).asRequired("Must provide a username")
                .withValidator(new RegexpValidator("Not a valid username", "^[a-zA-Z0-9]+$")).bind("username");

        binder.forField(password).asRequired("Password is not allowed to be empty")
                .withValidator(pass -> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
                        "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(user -> password.getEmptyValue(), (user, pass) -> {
                    if (!password.getEmptyValue().equals(pass)) {
                        user.setPasswordHash(passwordEncoder.encode(pass));
                    }
                });
        binder.forField(email).asRequired("Must provide an e-mail address").bind(User::getEmail, User::setEmail);

        topLayout.add(fullName);
        topLayout.add(username);
        topLayout.add(email);
        topLayout.add(password);
        topLayout.add(projectsLabel);
        topLayout.add(projectListBox);
        topLayout.add(rolesLabel);
        topLayout.add(rolesListBox);
    }

    @Override
    public Binder<User> createBinder() {
        return new Binder<>(User.class);
    }

    public void setRoleRepository(GenericRepository<Role> roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void setProjecRepository(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void editSpecificWidgets(User entity) {
        rolesListBox.setItems(roleRepository.findAll());
        if (null != currentEntity.getRoles()) {
            rolesListBox.select(this.currentEntity.getRoles());
        }
        projectListBox.setItems(projectRepository.findAll());
        if (null != this.currentEntity.getProjects()) {
            projectListBox.select(this.currentEntity.getProjects());
        }
    }

    @Override
    protected String getDefaultEntityName(User entiy) {
        return "User: " + entiy.getFullName();
    }

    @Override
    protected void forgetSpecificSelections() {
        rolesListBox.deselectAll();
        projectListBox.deselectAll();
    }

    @Override
    protected void saveSpecificEntities() {
    }

    @Override
    protected void deleteSpecificEntities() {
    }
}