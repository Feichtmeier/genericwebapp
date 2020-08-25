package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.Role;

public class RoleRestController extends GenericRestController<Role> {

    @Override
    protected Role updateEntity(Role e, Role newE) {
        
        e.setName(newE.getName());
        e.setPermissions(newE.getPermissions());

        return e;
    }
    
}