package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ArticleService {
    @Autowired
    private ArticleRepository articleRepository;

    public Long addArticle(Article article) {
        return articleRepository.addArticle(Article.of(article.getTitle(), article.getCoverUrl(), article.getContents()));
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> createNotFoundException(id));
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public void update(Article article) {
        Article articleFound = articleRepository.findById(article.getId())
            .orElseThrow(() -> createNotFoundException(article.getId()));
        articleFound.setTitle(article.getTitle());
        articleFound.setCoverUrl(article.getCoverUrl());
        articleFound.setContents(article.getContents());
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    private static NoSuchElementException createNotFoundException(Long id) {
        return new NoSuchElementException("Can't find article with id: " + id);
    }
}
