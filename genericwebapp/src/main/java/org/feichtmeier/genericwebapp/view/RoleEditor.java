package org.feichtmeier.genericwebapp.view;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public class RoleEditor extends GenericEntityEditor<Role> {

    private static final long serialVersionUID = -5462059994803015447L;

    private final TextField name;
    private final MultiSelectListBox<String> permissionListBox;

    private GenericRepository<Permission> permissionRepository;

    private Map<String, Permission> allPermissionsToPermissionViewNamesMap;

    public RoleEditor(Grid<Role> grid, GenericRepository<Role> roleRepository) {
        super(grid, roleRepository);
        name = new TextField("", "Name");
        permissionListBox = new MultiSelectListBox<>();
        add(name, permissionListBox);
        binder.bindInstanceFields(this);
        allPermissionsToPermissionViewNamesMap = new HashMap<>();
        permissionListBox.addSelectionListener(event -> {
            Set<String> selectedViewNames = event.getAllSelectedItems();
            Set<Permission> selectedPermissions = new HashSet<>();
            for (Map.Entry<String, Permission> entry : allPermissionsToPermissionViewNamesMap.entrySet()) {
                for (String viewName : selectedViewNames) {
                    if (viewName.equals(entry.getKey())) {
                        selectedPermissions.add(entry.getValue());
                    }
                }
            }
            currentEntity.setPermissions(selectedPermissions);
        });
    }

    @Override
    public Binder<Role> createBinder() {
        return new Binder<>(Role.class);
    }

	public void setPermissionRepository(GenericRepository<Permission> permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    protected void createSpecialWidgets(Role entity) {        
        Set<Permission> rolePermissions = new HashSet<>();
        if (entity.getPermissions() != null) {
            rolePermissions = entity.getPermissions();
        }
        List<Permission> allPermissions = permissionRepository.findAll();
        allPermissionsToPermissionViewNamesMap.clear();
        for (Permission permission : allPermissions) {
            allPermissionsToPermissionViewNamesMap.put(permission.getView().getName(), permission);
        }
        permissionListBox.setItems(allPermissionsToPermissionViewNamesMap.keySet());
        for (Map.Entry<String, Permission> entry : allPermissionsToPermissionViewNamesMap.entrySet()) {
            for (Permission permission : rolePermissions) {
                if (entry.getKey().equals(permission.getView().getName())) {
                    permissionListBox.select(permission.getView().getName());
                }
            }
        }

    }

}
