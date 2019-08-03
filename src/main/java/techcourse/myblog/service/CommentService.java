package techcourse.myblog.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.*;
import techcourse.myblog.dto.CommentDto;

@Slf4j
@Service
public class CommentService {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    public CommentService(ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }


    @Transactional
    public Comment find(long id, User user) {
        Comment comment = findById(id);
        comment.checkMatchUser(user);
        return comment;
    }

    @Transactional
    public Comment save(CommentDto commentDto, User user) {
        Article article = findArticleByArticleId(commentDto.getArticleId());
        return save(commentDto.toComment(user, article));
    }

    @Transactional
    public boolean isSuccessUpdate(long id, CommentDto commentDto, User user) {
        Comment preComment = commentRepository.findById(id).orElseThrow(RuntimeException::new);
        log.debug(">>> isSuccessUpdate : preComment : {}", preComment);

        try {
            preComment.update(commentDto, user);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }

    @Transactional
    public long deleteAndReturnArticleId(long id, User user) {
        Comment preComment = commentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        preComment.checkMatchUser(user);
        delete(preComment);
        return preComment.getArticleId();
    }

    private Comment findById(long id) {
        return commentRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    private Article findArticleByArticleId(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
    }

    private Comment save(Comment comment) {
        return commentRepository.save(comment);
    }

    private void delete(Comment comment) {
        commentRepository.delete(comment);
    }
}
