package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.grid.Grid;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.repository.GenericRepository;
import org.springframework.security.access.annotation.Secured;

@Secured(ViewNames.ROLE_VIEW)
public class RoleView extends GenericGridView<Role> {

    private static final long serialVersionUID = 5857470858968411471L;

    private RoleEditor roleEditor;

    public RoleView(GenericRepository<Role> roleRepository, GenericRepository<Permission> permissionRepository) {
        super(roleRepository);
        this.roleEditor.setPermissionRepository(permissionRepository);
    }

    @Override
    public Grid<Role> createGrid() {
        Grid<Role> roleGrid = new Grid<>(Role.class);
        roleGrid.removeAllColumns();
        roleGrid.addColumn("name");
        return roleGrid;
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
    protected void setViewName() {
        this.viewName = ViewNames.ROLE_VIEW;
    }

}
