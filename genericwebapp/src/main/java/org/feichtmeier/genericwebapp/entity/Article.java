package org.feichtmeier.genericwebapp.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class Article extends AbstractEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    private LocalDateTime timeStamp;
    @NotNull
    private String title;
    @NotNull
    private String textBody;
    @NotNull
    private String textBodyShort;
    @NotNull
    @OneToOne
    private User user;

    public Article() {
    }

    public Article(@NotNull LocalDateTime timeStamp, @NotNull String title, @NotNull String textBody,
            @NotNull String textBodyShort, @NotNull User user) {
        this.timeStamp = timeStamp;
        this.title = title;
        this.textBody = textBody;
        this.textBodyShort = textBodyShort;
        this.user = user;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTextBody() {
        return textBody;
    }

    public void setTextBody(String textBody) {
        this.textBody = textBody;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTextBodyShort() {
        return this.textBodyShort;
    }

    public void setTextBodyShort(String textBodyShort) {
        this.textBodyShort = textBodyShort;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((textBody == null) ? 0 : textBody.hashCode());
        result = prime * result + ((textBodyShort == null) ? 0 : textBodyShort.hashCode());
        result = prime * result + ((timeStamp == null) ? 0 : timeStamp.hashCode());
        result = prime * result + ((title == null) ? 0 : title.hashCode());
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
        Article other = (Article) obj;
        if (textBody == null) {
            if (other.textBody != null)
                return false;
        } else if (!textBody.equals(other.textBody))
            return false;
        if (textBodyShort == null) {
            if (other.textBodyShort != null)
                return false;
        } else if (!textBodyShort.equals(other.textBodyShort))
            return false;
        if (timeStamp == null) {
            if (other.timeStamp != null)
                return false;
        } else if (!timeStamp.equals(other.timeStamp))
            return false;
        if (title == null) {
            if (other.title != null)
                return false;
        } else if (!title.equals(other.title))
            return false;
        if (user == null) {
            if (other.user != null)
                return false;
        } else if (!user.equals(other.user))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Article [textBody=" + textBody + ", textBodyShort=" + textBodyShort + ", timeStamp=" + timeStamp
                + ", title=" + title + ", user=" + user + "]";
    }

}
