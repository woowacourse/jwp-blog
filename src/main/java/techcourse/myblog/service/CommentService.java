package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentAssembler;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.dto.comment.CommentRequest;
import techcourse.myblog.dto.comment.CommentResponse;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.exception.comment.CommentAuthenticationException;
import techcourse.myblog.exception.comment.CommentException;
import techcourse.myblog.repository.CommentRepository;

@Service
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private final CommentRepository commentRepository;
    private final ArticleService articleService;
    private final UserService userService;

    public CommentService(CommentRepository commentRepository, ArticleService articleService, UserService userService) {
        this.commentRepository = commentRepository;
        this.articleService = articleService;
        this.userService = userService;
    }

    @Transactional
    public CommentResponse addComment(CommentRequest commentRequest, UserResponse userResponse, Long articleId) {
        String contents = commentRequest.getContents();
        User commenter = userService.getUserByEmail(userResponse);
        Article article = articleService.findArticleById(articleId);

        Comment comment = commentRepository.save(new Comment(contents, commenter, article));
        article.addComment(comment);

        return CommentAssembler.toDto(comment);
    }

    @Transactional
    public CommentResponse update(Long commentId, CommentRequest commentRequest, UserResponse userResponse) {
        checkAuthentication(commentId, userResponse);
        Comment comment = getCommentFindById(commentId);

        comment.update(CommentAssembler.toEntity(commentRequest));

        return CommentAssembler.toDto(comment);
    }

    private Comment getCommentFindById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentException::new);
    }

    public void delete(Long commentId, UserResponse userResponse) {
        checkAuthentication(commentId, userResponse);
        commentRepository.deleteById(commentId);
    }

    public void checkAuthentication(Long commentId, UserResponse userResponse) {
        Comment comment = getCommentFindById(commentId);
        String commenterEmail = comment.getCommenter().getEmail();
        String userEmail = userResponse.getEmail();
        log.debug("userEmail : {}, commenterEmail : {} ", userEmail, commenterEmail);

        if (!userEmail.equals(commenterEmail)) {
            throw new CommentAuthenticationException();
        }
    }
}
