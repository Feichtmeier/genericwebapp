package org.feichtmeier.restapi.repository;

import java.util.List;

import org.feichtmeier.restapi.entity.Permission;
import org.feichtmeier.restapi.entity.View;

public interface PermissionRepository extends GenericRepository<Permission> {

    List<Permission> findByView(View view);
    
}