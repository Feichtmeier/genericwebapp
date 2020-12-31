package org.feichtmeier.restapi.controller;

import org.feichtmeier.genericwebapp.entity.Permission;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/permissions")
public class PermissionRestController extends GenericRestController<Permission> {

    @Override
    protected Permission updateEntity(Permission e, Permission newE) {
        e.setView(null != newE.getView() ? newE.getView() : e.getView());
        e.setEdit(newE.isEdit());
        
        return e;
    }
    
}