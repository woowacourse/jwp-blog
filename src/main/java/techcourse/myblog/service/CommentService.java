package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.exception.IllegalRequestException;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

@Slf4j
@Service
@Transactional
public class CommentService {

    private static final String NOT_FOUND_COMMENT_ERROR = "존재하지 않는 댓글";
    private static final String NOT_FOUND_ARTICLE_ERROR = "존재하지 않는 게시글";
    private static final String UNAUTHORIZED_USER_ERROR = "권한이 없는 사용자 입니다.";

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public Comment find(Long commentId) {
        return findCommentById(commentId);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(commentId));
                    return new NotFoundCommentException(NOT_FOUND_COMMENT_ERROR);
                });
    }

    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment save(CommentDto commentDto, User user, long articleId) {
        Article article = findArticleById(articleId);
        Comment comment = commentDto.toDomain(user, article);
        article.add(comment);
        return commentRepository.save(comment);
    }

    private Article findArticleById(Long articleId) {
        return articleRepository
                .findById(articleId)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(articleId));
                    return new NotFoundArticleException(NOT_FOUND_ARTICLE_ERROR);
                });
    }

    public void delete(Long commentId, User user) {
        Comment comment = getAuthorizedComment(commentId, user);
        commentRepository.delete(comment);
    }

    private Comment getAuthorizedComment(Long commentId, User user) {
        Comment comment = findCommentById(commentId);
        checkAuthorizedUser(user, comment);
        return comment;
    }

    private void checkAuthorizedUser(User user, Comment comment) {
        if (!comment.isAuthorized(user)) {
            throw new IllegalRequestException(UNAUTHORIZED_USER_ERROR);
        }
    }

    public void update(long commentId, CommentDto commentDto, User user) {
        Comment comment = getAuthorizedComment(commentId, user);
        comment.update(commentDto.getContents());
    }

    public List<Comment> findAllByArticleId(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }
}
