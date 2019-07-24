package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleSaveParams;
import techcourse.myblog.exception.ArticleNotFoundException;
import techcourse.myblog.repository.ArticleRepository;

import javax.transaction.Transactional;

@Service
public class ArticleService {
    private static final String ERROR_ARTICLE_NOT_FOUND_MESSAGE = "찾는 글이 존재하지 않습니다!";
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Iterable<Article> findAllArticles() {
        return articleRepository.findAll();
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Article findById(long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ArticleNotFoundException(ERROR_ARTICLE_NOT_FOUND_MESSAGE));
    }

    @Transactional
    public void update(ArticleSaveParams articleSaveParams, long id) {
        Article article = findById(id);
        article.update(articleSaveParams);
    }

    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }
}
