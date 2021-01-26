package org.feichtmeier.genericwebapp.entity;

import java.time.LocalDateTime;

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
}
