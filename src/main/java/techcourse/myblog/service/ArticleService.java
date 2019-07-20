package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Iterable<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public void save(Article article) {
        articleRepository.save(article);
    }

    public Article findById(long id) {
        return articleRepository.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public void update(Article article) {
        articleRepository.update(article);
    }

    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }
}
