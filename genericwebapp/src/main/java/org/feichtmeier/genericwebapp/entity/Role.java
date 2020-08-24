package org.feichtmeier.genericwebapp.entity;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Role
 */
@Entity
@Cacheable
public class Role extends AbstractEntity {

    /**
     *
     */
    private static final long serialVersionUID = -1560262625260195851L;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(name = "ROLE_TO_PERMISSION", joinColumns = @JoinColumn(name = "FK_PERMISSION_ID"), inverseJoinColumns = @JoinColumn(name = "FK_ROLE_ID"))
    Set<Permission> permissions;

    @JsonIgnore
    @ManyToMany(mappedBy = "roles", fetch=FetchType.EAGER)
    Set<User> users;

    @Column(nullable = false, unique = true)
    String name;

    public Role(String name) {
        this.name = name;
    }

    public Role() {
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
        Role other = (Role) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }

    

    

}