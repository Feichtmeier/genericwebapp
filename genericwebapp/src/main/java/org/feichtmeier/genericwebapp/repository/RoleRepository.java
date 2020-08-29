package org.feichtmeier.genericwebapp.repository;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.Role;

public interface RoleRepository extends GenericRepository<Role> {

    Role findByName(String name);
    List<Role> findByNameStartsWithIgnoreCase(String filterText);
    
}