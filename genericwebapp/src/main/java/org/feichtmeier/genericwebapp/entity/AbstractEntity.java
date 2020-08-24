package org.feichtmeier.genericwebapp.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

/**
 * AbstractEntity
 */

@MappedSuperclass
@JsonIgnoreType
public abstract class AbstractEntity implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -677720534539449813L;
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "ID", unique = true)
    long id;

    @Version
    private int version;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + version;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        AbstractEntity other = (AbstractEntity) obj;
        if (id != other.id)
            return false;
        if (version != other.version)
            return false;
        return true;
    }    
    
}