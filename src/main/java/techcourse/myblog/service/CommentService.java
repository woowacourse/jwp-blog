package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.service.exception.NoPermissionCommentException;
import techcourse.myblog.service.exception.NoRowException;
import techcourse.myblog.web.dto.CommentDto;

import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(NoRowException::new);
    }

    public List<Comment> findByArticle(Article article) {
        return commentRepository.findByArticle(article);
    }

    public Comment save(CommentDto commentDto, User author, Article article) {
        return commentRepository.save(commentDto.create(author, article));
    }

    public Comment update(Long commentId, CommentDto commentDto, User loginUser) {
        exist(commentId, loginUser);
        Comment comment = findById(commentId);
        return commentRepository.save(comment.update(commentDto));
    }

    public Long delete(Long commentId, User loginUser) {
        exist(commentId, loginUser);
        Long articleId = findById(commentId).getArticleId();
        commentRepository.deleteById(commentId);
        return articleId;
    }

    public void exist(Long commentId, User loginUser) throws NoPermissionCommentException {
        if (!commentRepository.existsByIdAndAuthor(commentId, loginUser)) {
            throw new NoPermissionCommentException("댓글에 대한 권한이 없습니다.");
        }
    }
}