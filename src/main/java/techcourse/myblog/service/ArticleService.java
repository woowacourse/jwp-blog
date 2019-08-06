package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.InvalidUserSessionException;
import techcourse.myblog.exception.UnFoundArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

@Slf4j
@Service
@Transactional
public class ArticleService {

    private static final String UNAUTHORIZED_USER_ERROR = "권한이 없습니다.";
    private static final String NOT_FOUND_ARTICLE_ERROR = "존재하지 않는 게시글";

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Article save(Article article) {
        return articleRepository.save(article);
    }

    public Article select(long id) {
        return findById(id);
    }

    public Iterable<Article> findAll() {
        return articleRepository.findAll();
    }

    public List<Comment> findCommentsByArticleId(long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    private Article findById(long id) {
        return articleRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(id));
                    return new UnFoundArticleException(NOT_FOUND_ARTICLE_ERROR);
                });
    }

    public void update(long id, Article updateArticle, User user) {
        Article article = getAuthorizedArticle(id, user);
        article.update(updateArticle);
    }

    private Article getAuthorizedArticle(long id, User user) {
        Article article = findById(id);
        checkAuthorizedUser(user, article);
        return article;
    }

    private void checkAuthorizedUser(User user, Article article) {
        if (user == null || !article.isAuthorized(user)) {
            throw new InvalidUserSessionException(UNAUTHORIZED_USER_ERROR);
        }
    }

    public void delete(long id, User user) {
        Article article = getAuthorizedArticle(id, user);
        articleRepository.delete(article);
    }
}
