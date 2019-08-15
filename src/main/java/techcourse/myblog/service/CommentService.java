package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.utils.CommentAssembler;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.service.dto.CommentResponse;
import techcourse.myblog.service.dto.CommentsResponse;
import techcourse.myblog.service.exception.InvalidAuthorException;
import techcourse.myblog.service.exception.NoCommentException;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);

    private static final NoCommentException NO_COMMENT_EXCEPTION = new NoCommentException("존재하지 않는 댓글입니다.");

    private final ArticleService articleService;
    private final CommentRepository commentRepository;
    private final CommentAssembler commentAssembler;

    public CommentService(ArticleService articleService, CommentRepository commentRepository, CommentAssembler commentAssembler) {
        this.articleService = articleService;
        this.commentRepository = commentRepository;
        this.commentAssembler = commentAssembler;
    }

    public CommentsResponse findByArticleId(Long articleId) {
        return new CommentsResponse(commentRepository.findByArticleId(articleId).stream()
                .map(CommentResponse::from)
                .collect(Collectors.toList()));
    }

    public CommentResponse save(CommentRequest commentRequest, User commenter) {
        Comment comment = commentAssembler.toCommentFromCommentRequestAndCommenter(commentRequest, commenter);

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.from(savedComment);
    }

    @Transactional
    public void update(CommentRequest commentRequest, User user, Long commentId) {
        Comment comment = findByIdWithUser(user, commentId);
        comment.updateContents(commentRequest.getContents());
    }

    private Comment findByIdWithUser(User user, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NoCommentException("존재하지 않는 댓글입니다."));
        // 검사하면 좋을 위치는??
        comment.checkCommenter(user);
        return comment;
    }

    public void deleteById(Long commentId) {
        log.debug("begin");

        commentRepository.deleteById(commentId);
    }

    private void validateCommentExistsAndUserIsCommenter(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> NO_COMMENT_EXCEPTION);

        if (!comment.isCommentor(user)) {
            throw new InvalidAuthorException("작성자가 일지하지 않습니다.");
        }
    }

    public boolean isExist(long commentId) {
        log.debug("begin");

        return commentRepository.findById(commentId)
                .isPresent();
    }

    public boolean isCommenter(long commentId, User user) {
        log.debug("begin");

        return commentRepository.findById(commentId)
                .map(comment -> comment.isCommentor(user))
                .orElse(false);
    }

    public CommentResponse findById(long commentId) {
        log.debug("begin");

        return commentRepository.findById(commentId)
                .map(CommentResponse::from)
                .orElseThrow(() -> NO_COMMENT_EXCEPTION);
    }
}
