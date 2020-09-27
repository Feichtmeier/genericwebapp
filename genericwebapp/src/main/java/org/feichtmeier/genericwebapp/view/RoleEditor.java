package org.feichtmeier.genericwebapp.view;

import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.repository.GenericRepository;

public class RoleEditor extends GenericEntityEditor<Role> {

    private static final long serialVersionUID = -5462059994803015447L;

    private final TextField name;
    private final DefaultSmallLabel permissionLabel;
    private final MultiSelectListBox<Permission> permissionListBox;

    private GenericRepository<Permission> permissionRepository;

    public RoleEditor(GenericRepository<Role> roleRepository, GenericGridView<Role> view) {
        super(roleRepository, view);
        name = new TextField("");
        name.setLabel("Name of the role");
        name.setWidthFull();
        permissionListBox = new MultiSelectListBox<>();
        permissionListBox.setWidthFull();
        permissionLabel = new DefaultSmallLabel("Allowed views");
        permissionLabel.setWidthFull();
        topLayout.add(name);
        topLayout.add(permissionLabel);
        topLayout.add(permissionListBox);
        binder.forField(name).asRequired("Must chose a role name").bind(Role::getName, Role::setName);

        permissionListBox.addSelectionListener(e -> {
            currentEntity.setPermissions(e.getValue());
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
    protected void editSpecificWidgets(Role entity) {
        permissionListBox.setItems(permissionRepository.findAll());
        if (null != entity.getPermissions()) {
            permissionListBox.select(entity.getPermissions());
        }

    }

    @Override
    protected String getDefaultEntityName(Role entiy) {
        return "Role: " + entiy.getName();
    }

    @Override
    protected void forgetSpecificSelections() {
        permissionListBox.deselectAll();
    }

    @Override
    protected void saveSpecificEntities() {
    }

    @Override
    protected void deleteSpecificEntities() {
    }

}
