package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.repository.PermissionRepository;
import org.springframework.stereotype.Service;

@Service
public class PermissionService implements DataService<Permission> {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public Permission save(Permission entity) {
        return permissionRepository.save(entity);
    }

    @Override
    public void delete(Permission entity) {
        permissionRepository.delete(entity);
    }

    @Override
    public Permission getOne(Permission entity) {
        return permissionRepository.getOne(entity.getId());
    }

}
