package org.feichtmeier.genericwebapp.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Project extends AbstractEntity {

    private static final long serialVersionUID = 5219845253466363534L;

    @NotNull
    @Column(unique = true, length = 255)
    private String name;

    @ManyToMany(mappedBy = "projects", fetch = FetchType.EAGER)
    @JsonIgnore
    private Set<User> users;

    @OneToOne(mappedBy = "project")
    ProjectImage projectImage;

    public Project(@NotNull String name) {
        this.name = name;
    }

    public Project() {
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public ProjectImage getProjectImage() {
        return projectImage;
    }

    public void setProjectImage(ProjectImage projectImage) {
        this.projectImage = projectImage;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((projectImage == null) ? 0 : projectImage.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Project other = (Project) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (projectImage == null) {
            if (other.projectImage != null)
                return false;
        } else if (!projectImage.equals(other.projectImage))
            return false;
        return true;
    }
}
