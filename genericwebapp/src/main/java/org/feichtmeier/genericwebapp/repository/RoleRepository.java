package org.feichtmeier.genericwebapp.repository;

import org.feichtmeier.genericwebapp.entity.Role;

public interface RoleRepository extends GenericRepository<Role> {

    Role findByName(String name);
    
}