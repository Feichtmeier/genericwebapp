package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionController extends GenericRestController<Permission> {

    @Override
    protected Permission updateEntity(Permission e, Permission newE) {
        e.setEdit(newE.isEdit());
        e.setView(newE.getView());
        return e;
    }
    
}