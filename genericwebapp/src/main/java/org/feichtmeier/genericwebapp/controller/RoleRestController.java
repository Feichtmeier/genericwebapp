package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleRestController extends GenericRestController<Role> {

    @Override
    protected Role updateEntity(Role e, Role newE) {
        
        e.setName(newE.getName());
        e.setPermissions(newE.getPermissions());

        return e;
    }
    
}