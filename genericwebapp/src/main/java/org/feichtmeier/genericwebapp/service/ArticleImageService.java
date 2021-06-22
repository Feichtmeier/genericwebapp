package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.ArticleImage;
import org.feichtmeier.genericwebapp.repository.ArticleImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticleImageService implements DataService<ArticleImage> {

    private final ArticleImageRepository articleImageRepository;

    public ArticleImageService(ArticleImageRepository articleImageRepository) {
        this.articleImageRepository = articleImageRepository;
    }

    @Override
    public List<ArticleImage> findAll() {
        return articleImageRepository.findAll();
    }

    @Override
    public ArticleImage save(ArticleImage entity) {
        return articleImageRepository.save(entity);
    }

    @Override
    public void delete(ArticleImage entity) {
        articleImageRepository.delete(entity);
    }

    @Override
    public ArticleImage getOne(ArticleImage entity) {
        return articleImageRepository.getOne(entity.getId());
    }
    
}
