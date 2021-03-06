package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.Role;
import org.feichtmeier.genericwebapp.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements DataService<Role> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role save(Role entity) {
        return roleRepository.save(entity);
    }

    @Override
    public void delete(Role entity) {
        roleRepository.delete(entity);
    }

    @Override
    public Role getOne(Role entity) {
        return roleRepository.getOne(entity.getId());
    }

    

    
}
