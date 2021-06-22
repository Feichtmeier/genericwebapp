package org.feichtmeier.genericwebapp.service;

import java.util.List;

import org.feichtmeier.genericwebapp.entity.Article;
import org.feichtmeier.genericwebapp.repository.ArticleRepository;
import org.springframework.stereotype.Service;

@Service
public class ArticleService implements DataService<Article> {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public List<Article> findAll() {        
        return articleRepository.findAll();
    }

    @Override
    public Article save(Article entity) {
        return articleRepository.save(entity);

    }

    @Override
    public void delete(Article entity) {
        articleRepository.delete(entity);
    }

    @Override
    public Article getOne(Article entity) {
        return articleRepository.getOne(entity.getId());
    }

    public boolean exists(Article entity) {
        return articleRepository.existsById(entity.getId());
    }
    
}
