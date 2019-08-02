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


    public Article findArticleByArticleId(long articleId) {
        return articleRepository.findById(articleId).orElseThrow(RuntimeException::new);
    }

    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public Comment findCommentById(long id) {
        return commentRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }

    @Transactional
    public boolean isSuccessUpdate(CommentDto commentDto, User user) {
        Comment preComment = commentRepository.findById(commentDto.getId()).orElseThrow(RuntimeException::new);
        log.debug(">>> isSuccessUpdate : preComment : {}", preComment);

        try {
            preComment.update(commentDto, user);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}
