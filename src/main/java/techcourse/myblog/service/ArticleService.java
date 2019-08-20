package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.ArticleRequest;
import techcourse.myblog.service.exception.NotFoundArticleException;

import javax.transaction.Transactional;

@Service
public class ArticleService {
    private static final Logger log = LoggerFactory.getLogger(ArticleService.class);

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Page<Article> findAll(Pageable pageable) {
        log.debug("begin");

        return articleRepository.findAll(pageable);
    }

    public Article save(ArticleRequest articleRequest, User user) {
        Article article = articleRequest.addAuthorAndToArticle(user);
        return articleRepository.save(article);
    }

    public Article findById(long articleId) {
        return articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundArticleException(NotFoundArticleException.NO_ARTICLE_MSG));
    }

    public Article findByIdWithUser(long articleId, User user) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new NotFoundArticleException(NotFoundArticleException.NO_ARTICLE_MSG));
        article.checkAuthor(user);

        return article;
    }

    @Transactional
    public Article editArticle(ArticleRequest articleRequest, long articleId, User user) {
        log.debug("begin");

        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        log.debug("article: {}", article);
        log.debug("articleRequest: {}", articleRequest);

        article.updateArticle(articleRequest.toArticle(), user);
        return article;
    }

    public void deleteById(long articleId, User user) {
        log.debug("begin");

        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.checkAuthor(user);
        articleRepository.deleteById(articleId);
    }
}
