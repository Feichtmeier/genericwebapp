package org.feichtmeier.restapi.repository;

import java.util.List;

import org.feichtmeier.restapi.entity.Role;

public interface RoleRepository extends GenericRepository<Role> {

    Role findByName(String name);
    List<Role> findByNameStartsWithIgnoreCase(String filterText);
    
}