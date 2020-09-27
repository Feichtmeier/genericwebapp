package org.feichtmeier.genericwebapp.entity;

import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectImage extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] byteArray;

    @NotNull
    private String fileName;

    @NotNull
    private String MIMEType;

    @OneToOne
    private Project project;

    public ProjectImage() {
    }    

    public ProjectImage(byte[] byteArray, @NotNull String fileName, @NotNull String mIMEType, Project project) {
        this.byteArray = byteArray;
        this.fileName = fileName;
        MIMEType = mIMEType;
        this.project = project;
    }

    public byte[] getByteArray() {
        return byteArray;
    }

    public void setByteArray(byte[] byteArray) {
        this.byteArray = byteArray;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getMIMEType() {
        return MIMEType;
    }

    public void setMIMEType(String mIMEType) {
        MIMEType = mIMEType;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((MIMEType == null) ? 0 : MIMEType.hashCode());
        result = prime * result + Arrays.hashCode(byteArray);
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
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
        ProjectImage other = (ProjectImage) obj;
        if (MIMEType == null) {
            if (other.MIMEType != null)
                return false;
        } else if (!MIMEType.equals(other.MIMEType))
            return false;
        if (!Arrays.equals(byteArray, other.byteArray))
            return false;
        if (fileName == null) {
            if (other.fileName != null)
                return false;
        } else if (!fileName.equals(other.fileName))
            return false;
        return true;
    }

    
    
}
