package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.grid.Grid;

import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

@Secured(ViewNames.USER_VIEW)
public class UserView extends GenericGridView<User> {

    private static final long serialVersionUID = 5168974008835497598L;

    private UserEditor userEditor;

    public UserView(GenericRepository<User> userRepository, GenericRepository<Role> roleRepository, PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userEditor.setRoleRepository(roleRepository);
        this.userEditor.setPasswordEncoder(passwordEncoder);        
    }

    @Override
    public Grid<User> createGrid() {
        return new Grid<>(User.class);
    }

    @Override
    public User createEmptyEntity() {
        return new User("", "");
    }

    @Override
    public UserEditor createEditor() {
        UserEditor userEditor = new UserEditor(this.grid, this.repository);
        this.userEditor = userEditor;
        return userEditor;
    }

    @Override
    protected String[] createWantedColumnNames() {
        String[] columnNames = {"username", "fullName"};
        return columnNames;
    }

    @Override
    protected void setViewName() {
        this.viewName = ViewNames.USER_VIEW;
    }
    
}