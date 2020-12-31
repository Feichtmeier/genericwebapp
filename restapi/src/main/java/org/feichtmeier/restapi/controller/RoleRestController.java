package org.feichtmeier.restapi.controller;

import org.feichtmeier.genericwebapp.entity.Role;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/roles")
public class RoleRestController extends GenericRestController<Role> {

    @Override
    protected Role updateEntity(Role e, Role newE) {

        e.setName(null != newE.getName() ? newE.getName() : e.getName());
        e.setPermissions(null != newE.getPermissions() ? newE.getPermissions() : e.getPermissions());
        e.setUsers(null != newE.getUsers() ? newE.getUsers() : e.getUsers());

        return e;
    }

}