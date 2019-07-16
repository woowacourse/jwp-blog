package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.NoSuchElementException;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article addArticle(Article article) {
        return articleRepository.save(Article.of(article.getTitle(), article.getCoverUrl(), article.getContents()));
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
            .orElseThrow(() -> createNotFoundException(id));
    }

    public Iterable<Article> findAll() {
        return articleRepository.findAll();
    }

    public Article update(Article article) {
        return articleRepository.save(article);
    }

    public void deleteById(Long id) {
        articleRepository.deleteById(id);
    }

    private static NoSuchElementException createNotFoundException(Long id) {
        return new NoSuchElementException("Can't find article with id: " + id);
    }
}
