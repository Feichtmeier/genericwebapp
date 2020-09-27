package org.feichtmeier.genericwebapp.repository;

import org.feichtmeier.genericwebapp.entity.Project;

public interface ProjectRepository extends GenericRepository<Project> {

    Project findByName(String name);
    
}
