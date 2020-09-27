package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.Project;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projects")
public class ProjectRestController extends GenericRestController<Project> {

    @Override
    protected Project updateEntity(Project e, Project newE) {
        e.setName(null != newE.getName() ? newE.getName() : e.getName());
        e.setUsers(null != newE.getUsers() ? newE.getUsers() : e.getUsers());
        e.setProjectImage(null != newE.getProjectImage() ? newE.getProjectImage() : e.getProjectImage());
        return e;
    }
    
    
}
