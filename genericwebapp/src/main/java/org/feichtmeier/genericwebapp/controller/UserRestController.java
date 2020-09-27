package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController extends GenericRestController<User> {

    @Override
    protected User updateEntity(User e, User newE) {
        e.setFullName(null != newE.getFullName() ? newE.getFullName() : e.getFullName());
        e.setUsername(null != newE.getUsername() ? newE.getUsername() : e.getUsername());
        e.setEmail(null != newE.getEmail() ? newE.getEmail() : e.getEmail());
        e.setPasswordHash(null != newE.getPasswordHash() ? newE.getPasswordHash() : e.getPasswordHash());
        e.setRoles(null != newE.getRoles() ? newE.getRoles() : e.getRoles());
        e.setProjects(null != newE.getProjects() ? newE.getProjects() : e.getProjects());

        return e;
    }    
}