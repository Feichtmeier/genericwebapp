package org.feichtmeier.genericwebapp.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends AbstractEntity {

    private static final long serialVersionUID = -8628945301913035776L;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_TO_ROLE", joinColumns = @JoinColumn(name = "FK_USER_ID"), inverseJoinColumns = @JoinColumn(name = "FK_ROLE_ID"))
    private Set<Role> roles;

    @NotNull
    @Column(unique = true)
    private String username;

    @NotNull
    private String fullName;

    @NotNull
    @Size(min = 4, max = 255)
    private String passwordHash;

    @NotEmpty
    @Email
    @Size(max = 255)
    @Column(unique = true)
    private String email;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "USER_TO_PROJECT", joinColumns = @JoinColumn(name = "FK_USER_ID"), inverseJoinColumns = @JoinColumn(name = "FK_PROJECT_ID"))
    private Set<Project> projects;

    private boolean locked = false;

    public User() {
    }

    public User(@NotNull String username, @NotNull String fullName,
            @NotNull @Size(min = 4, max = 255) String passwordHash, @NotEmpty @Email @Size(max = 255) String email,
            boolean locked, @NotNull Set<Project> projects) {
        this.username = username;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
        this.email = email;
        this.locked = locked;
        this.projects = projects;
    }

    public User(@NotNull String username, @NotNull String fullName) {
        this.username = username;
        this.fullName = fullName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Set<Project> getProjects() {
        return this.projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((fullName == null) ? 0 : fullName.hashCode());
        result = prime * result + (locked ? 1231 : 1237);
        result = prime * result + ((passwordHash == null) ? 0 : passwordHash.hashCode());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
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
        User other = (User) obj;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (fullName == null) {
            if (other.fullName != null)
                return false;
        } else if (!fullName.equals(other.fullName))
            return false;
        if (locked != other.locked)
            return false;
        if (passwordHash == null) {
            if (other.passwordHash != null)
                return false;
        } else if (!passwordHash.equals(other.passwordHash))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        } else if (!username.equals(other.username))
            return false;
        return true;
    }
}