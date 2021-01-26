package org.feichtmeier.genericwebapp.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class ArticleImage extends AbstractImage {

    private static final long serialVersionUID = 796825114581051545L;

    @NotNull
    @OneToOne
    private Article article;

    public ArticleImage(byte[] byteArray, @NotNull String fileName, @NotNull String mIMEType,
            @NotNull Article article, @NotNull LocalDateTime timeStamp) {
        super(byteArray, fileName, mIMEType, timeStamp);
        this.article = article;
    }

    public ArticleImage() { }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((article == null) ? 0 : article.hashCode());
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
        ArticleImage other = (ArticleImage) obj;
        if (article == null) {
            if (other.article != null)
                return false;
        } else if (!article.equals(other.article))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "ArticleImage [article=" + article + "]";
    };
}
