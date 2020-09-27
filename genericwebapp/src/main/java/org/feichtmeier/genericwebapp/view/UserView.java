package org.feichtmeier.genericwebapp.view;

import java.util.List;

import com.vaadin.flow.component.grid.Grid;

import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.entity.User;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.feichtmeier.genericwebapp.repository.ProjectRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;

@Secured(ViewNames.USER_VIEW)
public class UserView extends GenericGridView<User> implements UserFilter {

    private static final long serialVersionUID = 5168974008835497598L;

    private UserEditor userEditor;

    public UserView(GenericRepository<User> userRepository, GenericRepository<Role> roleRepository, ProjectRepository projectRepository,
            PasswordEncoder passwordEncoder) {
        super(userRepository);
        this.userEditor.setRoleRepository(roleRepository);
        this.userEditor.setProjecRepository(projectRepository);
        this.userEditor.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public Grid<User> createGrid() {
        Grid<User> userGrid = new Grid<>(User.class);
        userGrid.removeAllColumns();
        userGrid.addColumn("username").setFooter("Users: " + repository.count());
        userGrid.addColumn("fullName");
        return userGrid;
    }

    @Override
    public User createEmptyEntity() {
        return new User("", "");
    }

    @Override
    public UserEditor createEditor() {
        UserEditor userEditor = new UserEditor(this.repository, this);
        this.userEditor = userEditor;
        return userEditor;
    }

    @Override
    protected List<User> mainFilterOperation(String filterText) {
        return listUsersByFullName(filterText, getAllowedEntities());
    }

    @Override
    public List<User> getAllowedEntities() {
        return listUserByProjectName(AppView.GLOBAL_PROJECT.getName(), repository.findAll());
    }
    
}