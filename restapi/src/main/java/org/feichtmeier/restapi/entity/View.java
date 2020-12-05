package org.feichtmeier.restapi.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * View
 */
@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class View extends AbstractEntity {

    /**
     *
     */
    private static final long serialVersionUID = -7794725293820392762L;

    @NotNull
    private String name;

    @OneToOne(mappedBy = "view", cascade = CascadeType.ALL)
    @JsonIgnore
    private Permission permission;

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