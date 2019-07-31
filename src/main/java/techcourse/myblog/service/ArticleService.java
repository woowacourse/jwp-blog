package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.exception.InvalidUserSessionException;
import techcourse.myblog.exception.UnFoundArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

@Slf4j
@Service
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public Article save(Article article, User user) {
        article.setAuthor(user);
        return articleRepository.save(article);
    }

    public Article select(long id) {
        return findById(id);
    }

    private Article findById(long id) {
        return articleRepository
                .findById(id)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(id));
                    throw new UnFoundArticleException("해당 게시글이 존재하지 않습니다.");
                });
    }

    public void update(long id, Article updateArticle, User user) {
        Article article = findById(id);
        checkAuthorizedUser(user, article);
        article.update(updateArticle);
    }

    private void checkAuthorizedUser(User user, Article article) {
        if (user == null || !user.equals(article.getAuthor())) {
            throw new InvalidUserSessionException("권한이 없습니다.");
        }
    }

    public void delete(long id, User user) {
        Article article = findById(id);
        checkAuthorizedUser(user, article);
        commentRepository
                .findAllByArticleId(id)
                .forEach(commentRepository::delete);
        articleRepository.delete(article);
    }
}
