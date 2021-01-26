package org.feichtmeier.genericwebapp.entity;

import java.time.LocalDateTime;
import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class AvatarImage extends AbstractImage {

    private static final long serialVersionUID = 1L;

    @NotNull
    @OneToOne
    private User user;

    public AvatarImage() {
    }

    public AvatarImage(byte[] byteArray, @NotNull String fileName, @NotNull String mIMEType, @NotNull User user,
            @NotNull LocalDateTime timeStamp) {
        super(byteArray, fileName, mIMEType, timeStamp);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((user == null) ? 0 : user.hashCode());
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
        AvatarImage other = (AvatarImage) obj;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "AvatarImage [user=" + user + "]";
    }
}
