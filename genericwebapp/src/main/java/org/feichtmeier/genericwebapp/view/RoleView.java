package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.grid.Grid;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public class RoleView extends GenericGridView<Role> {

    private static final long serialVersionUID = 5857470858968411471L;

    private RoleEditor roleEditor;

    public RoleView(GenericRepository<Role> roleRepository, GenericRepository<Permission> permissionRepository) {
        super(roleRepository);
        this.roleEditor.setPermissionRepository(permissionRepository);
    }

    @Override
    public Grid<Role> createGrid() {
        return new Grid<>(Role.class);
    }

    @Override
    public GenericEntityEditor<Role> createEditor() {
        RoleEditor roleEditor = new RoleEditor(this.grid, this.repository);
        this.roleEditor = roleEditor;
        return roleEditor;
    }

    @Override
    public Role createEmptyEntity() {
        return new Role("");
    }

    @Override
    protected String[] createWantedColumnNames() {
        String[] columnNames = {"name"};
        return columnNames;
    }

}
