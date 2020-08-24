package org.feichtmeier.genericwebapp.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.cache.annotation.Cacheable;

@Entity
@Cacheable
public class Permission extends AbstractEntity {

    /**
     *
     */
    private static final long serialVersionUID = -3857939939306694988L;

    @JsonIgnore
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    Set<Role> roles;

    @OneToOne(fetch=FetchType.EAGER)
    @JoinColumn
    View view;

    @Column(columnDefinition = "tinyint(1) default 1")
    private boolean edit = true;

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Permission other = (Permission) obj;
        if (edit != other.edit)
            return false;
        if (view == null) {
            if (other.view != null)
                return false;
        } else if (!view.equals(other.view))
            return false;
        return true;
    }

    

    

    
}
