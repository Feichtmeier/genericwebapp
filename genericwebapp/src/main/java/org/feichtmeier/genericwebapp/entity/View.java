package org.feichtmeier.genericwebapp.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * View
 */
@Entity
@Cacheable
public class View extends AbstractEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7794725293820392762L;

    String name;

    @JsonIgnore
    @OneToOne(mappedBy = "view")
    private Permission permission;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }

    public View() {
        
    }

    public View(String name, Permission permission) {
        this.name = name;
        this.permission = permission;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        View other = (View) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (permission == null) {
            if (other.permission != null)
                return false;
        } else if (!permission.equals(other.permission))
            return false;
        return true;
    }

    

}