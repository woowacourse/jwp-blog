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
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.repository.CommentRepository;

@Slf4j
@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    public List<Comment> findAll(Long articleId) {
        return commentRepository.findAllByArticleId(articleId);
    }

    public Comment find(Long commentId) {
        return findCommentById(commentId);
    }

    private Comment findCommentById(Long commentId) {
        return commentRepository
                .findById(commentId)
                .orElseThrow(() -> {
                    log.debug(String.valueOf(commentId));
                    return new NotFoundCommentException("존재하지 않는 댓글");
                });
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
                    return new RuntimeException("존재하지 않는 게시글");
                });
    }

    public void delete(Long commentId, User user, Long articleId) {
        Comment comment = findCommentById(commentId);
        checkAuthorizedUser(user, comment);
        findArticleById(articleId)
                .remove(comment);
        commentRepository.delete(comment);
    }

    private void checkAuthorizedUser(User user, Comment comment) {
        if (!comment.isAuthorized(user)) {
            throw new IllegalRequestException("권한이 없는 사용자 입니다.");
        }
    }

    public void update(long commentId, CommentDto commentDto, User user) {
        Comment comment = findCommentById(commentId);
        checkAuthorizedUser(user, comment);
        comment.update(commentDto.getContents());
    }
}
