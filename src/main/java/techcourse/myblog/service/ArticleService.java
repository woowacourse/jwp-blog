package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.exception.ArticleNotFoundException;
import techcourse.myblog.persistence.ArticleRepository;

import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(long id) {
        return articleRepository.findById(id).orElseThrow(ArticleNotFoundException::new);
    }

    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }
}
