package org.feichtmeier.genericwebapp.repository;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.feichtmeier.genericwebapp.entity.View;

public interface PermissionRepository extends GenericRepository<Permission> {

    List<Permission> findByView(View view);
    
}