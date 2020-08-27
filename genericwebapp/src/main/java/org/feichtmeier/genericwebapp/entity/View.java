package org.feichtmeier.genericwebapp.entity;

import javax.persistence.Cacheable;
import javax.persistence.Entity;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public View() {
        
    }

    public View(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
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
        View other = (View) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

}