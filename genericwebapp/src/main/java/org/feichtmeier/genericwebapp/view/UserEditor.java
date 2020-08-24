package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.validator.RegexpValidator;

import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserEditor extends GenericEntityEditor<User> {

    PasswordEncoder passwordEncoder;

    private static final long serialVersionUID = -7224805495412375614L;

    private final TextField fullName;
    private final TextField username;
    private final EmailField email;
    private final PasswordField password;
    private final MultiSelectListBox<String> rolesListBox;

    private GenericRepository<Role> roleRepository;

    private Map<String, Role> allRolesToRoleNamesMap;

    public UserEditor(Grid<User> grid, GenericRepository<User> userRepository) {
        super(grid, userRepository);

        fullName = new TextField("", "Full Name");
        username = new TextField("", "Username");
        email = new EmailField("", "E-Mail");
        password = new PasswordField("", "Password");
        rolesListBox = new MultiSelectListBox<>();

        binder.forField(fullName).withValidator(new RegexpValidator("Not a valid full name", "(?i)[a-z]+(\\s+[a-z]+)*"))
                .bind("fullName");

        binder.forField(username).withValidator(new RegexpValidator("Not a valid username", "^[a-zA-Z0-9]+$"))
                .bind("username");

        binder.forField(password)
                .withValidator(pass -> pass.matches("^(|(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{6,})$"),
                        "need 6 or more chars, mixing digits, lowercase and uppercase letters")
                .bind(user -> password.getEmptyValue(), (user, pass) -> {
                    if (!password.getEmptyValue().equals(pass)) {
                        user.setPasswordHash(passwordEncoder.encode(pass));
                    }
                });
        binder.bind(email, "email");

        this.cancelButton.addClickListener(e -> {
            rolesListBox.deselectAll();
        });

        allRolesToRoleNamesMap = new HashMap<>();
        rolesListBox.addSelectionListener(event -> {
            Set<String> selectedRoleNames = event.getAllSelectedItems();
            Set<Role> selectedRoles = new HashSet<>();
            for (Map.Entry<String, Role> entry : allRolesToRoleNamesMap.entrySet()) {
                for (String roleName : selectedRoleNames) {
                    if (roleName.equals(entry.getKey())) {
                        selectedRoles.add(entry.getValue());
                    }
                }
            }
            currentEntity.setRoles(selectedRoles);
        });

        add(fullName, username, email, password, rolesListBox);
    }

    @Override
    public Binder<User> createBinder() {
        return new Binder<>(User.class);
    }

    public void setRoleRepository(GenericRepository<Role> roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void editEntity(User user) {
        if (user == null) {
            return;
        }
        rolesListBox.deselectAll();

        this.currentEntity = user;
        binder.setBean(this.currentEntity);

        Set<Role> userRoles = new HashSet<>();
        if (user.getRoles() != null) {
            userRoles = user.getRoles();
        }
        List<Role> allRoles = roleRepository.findAll();
        allRolesToRoleNamesMap.clear();
        for (Role role : allRoles) {
            allRolesToRoleNamesMap.put(role.getName(), role);
        }
        rolesListBox.setItems(allRolesToRoleNamesMap.keySet());
        for (Map.Entry<String, Role> entry : allRolesToRoleNamesMap.entrySet()) {
            for (Role role : userRoles) {
                if (entry.getKey().equals(role.getName())) {
                    rolesListBox.select(role.getName());
                }
            }
        }

        this.setVisible(true);
        saveButton.setVisible(true);
    }

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
	}
}