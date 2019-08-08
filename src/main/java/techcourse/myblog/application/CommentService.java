package techcourse.myblog.application;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import techcourse.myblog.application.dto.CommentRequest;
import techcourse.myblog.application.exception.CommentNotFoundException;
import techcourse.myblog.application.exception.EmptyCommentRequestException;
import techcourse.myblog.application.exception.NotSameAuthorException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.repository.CommentRepository;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    private final ArticleService articleService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, ArticleService articleService, UserService userService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userService = userService;
    }

    public Comment save(CommentRequest commentRequest, Long articleId, Long userId) {
        if (StringUtils.isBlank(commentRequest.getContents())) {
            throw new EmptyCommentRequestException();
        }

        Article article = articleService.findById(articleId);

        User user = userService.findById(userId);

        Comment comment = new Comment(commentRequest.getContents(), user, article);

        return commentRepository.save(comment);
    }

    public List<Comment> findByArticle(Article article) {
        return Collections.unmodifiableList(commentRepository.findAllByArticle(article));
    }

    public Comment findById(Long commentId) {
        return commentRepository.findById(commentId)
            .orElseThrow(() -> new CommentNotFoundException("존재하지 않는 댓글입니다."));
    }

    public void delete(Long commentId, Long userId) {
        Comment comment = findById(commentId);

        if (!comment.matchAuthorId(userId)) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 삭제할 수 있습니다.");
        }

        commentRepository.deleteById(commentId);
    }

    @Transactional
    public void updateComment(Long commentId, Long userId, CommentRequest commentRequest) {
        Comment comment = findById(commentId);
        User author = userService.findById(userId);
        Comment updatedComment = commentRequest.toEntity(author, comment.getArticle());

        if (!comment.matchAuthorId(userId)) {
            throw new NotSameAuthorException("해당 작성자만 댓글을 수정할 수 있습니다.");
        }
        comment.updateContents(updatedComment);
    }

}
