package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.UnFoundArticleException;
import techcourse.myblog.repository.ArticleRepository;

@Slf4j
@Service
@Transactional
public class ArticleService extends AbstractDomainService<Article> {

    private static final String NOT_FOUND_ARTICLE_ERROR = "존재하지 않는 게시글";

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article select(Long id) {
        return findById(id);
    }

    private Article findById(Long id) {
        return articleRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(id));
                    return new UnFoundArticleException(NOT_FOUND_ARTICLE_ERROR);
                });
    }

    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Article update(Long id, Article updateArticle, User user) {
        Article article = getAuthorizedDomain(id, user);
        return article.update(updateArticle);
    }

    @Override
    protected Article getAuthorizedDomain(Long id, User user) {
        Article article = findById(id);
        checkAuthorizedUser(user, article);
        return article;
    }

    @Override
    public void delete(Long id, User user) {
        Article article = getAuthorizedDomain(id, user);
        articleRepository.delete(article);
    }
}
