package org.feichtmeier.genericwebapp.controller;

import org.feichtmeier.genericwebapp.entity.ProjectImage;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/projectImages")
public class ProjectImageRestController extends GenericRestController<ProjectImage>{

    @Override
    protected ProjectImage updateEntity(ProjectImage e, ProjectImage newE) {

        e.setByteArray(null != newE.getByteArray() ? newE.getByteArray() : e.getByteArray());
        e.setFileName(null != newE.getFileName() ? newE.getFileName() : e.getFileName());
        e.setMIMEType(null != newE.getMIMEType() ? newE.getMIMEType() : e.getMIMEType());
        e.setProject(null != newE.getProject() ? newE.getProject() : e.getProject());

        return e;
    }
    
}
