package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.service.dto.CommentResponse;
import techcourse.myblog.service.dto.CommentsResponse;
import techcourse.myblog.service.exception.NoCommentException;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private final ArticleService articleService;
    private final CommentRepository commentRepository;

    public CommentService(ArticleService articleService, CommentRepository commentRepository) {
        this.articleService = articleService;
        this.commentRepository = commentRepository;
    }

    public Comment save(CommentRequest commentRequest, User user) {
        Comment comment = new Comment(commentRequest.getContents(),
                articleService.findById(commentRequest.getArticleId()), user);
        return commentRepository.save(comment);
    }

    public CommentsResponse findByArticleId(Long articleId) {
        return new CommentsResponse(commentRepository.findByArticleId(articleId).stream()
                .map(comment -> new CommentResponse(comment.getId(),
                        comment.getContents(),
                        comment.getCreatedDate(),
                        comment.getCommenter()))
                .collect(Collectors.toList()));
    }

    @Transactional
    public Comment update(CommentRequest commentRequest, User user, Long commentId) {
        Comment comment = findByIdWithUser(user, commentId);
        return comment.updateContents(commentRequest.getContents());
    }

    private Comment findByIdWithUser(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoCommentException("존재하지 않는 댓글입니다."));
        comment.checkCommenter(user);
        return comment;
    }

    public void deleteById(Long commentId, User user) {
        findByIdWithUser(user, commentId);
        commentRepository.deleteById(commentId);
    }
}
