package org.feichtmeier.genericwebapp.entity;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.persistence.Basic;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@SuppressWarnings("serial")
@MappedSuperclass
public abstract class AbstractImage extends AbstractEntity {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    protected byte[] byteArray;

    @NotNull
    protected String fileName;

    @NotNull
    protected String MIMEType;

    @NotNull
    protected LocalDateTime timeStamp;

    public AbstractImage() {
    }

    public AbstractImage(byte[] byteArray, @NotNull String fileName, @NotNull String mIMEType,
            @NotNull LocalDateTime timeStamp) {
        this.byteArray = byteArray;
        this.fileName = fileName;
        MIMEType = mIMEType;
        this.timeStamp = timeStamp;
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

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((MIMEType == null) ? 0 : MIMEType.hashCode());
        result = prime * result + Arrays.hashCode(byteArray);
        result = prime * result + ((fileName == null) ? 0 : fileName.hashCode());
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
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
        AbstractImage other = (AbstractImage) obj;
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
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        } else if (!timeStamp.equals(other.timeStamp))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AbstractImage [MIMEType=" + MIMEType + ", fileName=" + fileName + ", timeStamp=" + timeStamp + "]";
    }
}
