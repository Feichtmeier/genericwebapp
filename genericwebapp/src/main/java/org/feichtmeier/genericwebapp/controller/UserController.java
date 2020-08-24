package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController extends GenericRestController<User> {

    @Override
    protected User updateEntity(User e, User newE) {
        e.setFullName(newE.getFullName());
        e.setUsername(newE.getUsername());

        return e;
    }    
}