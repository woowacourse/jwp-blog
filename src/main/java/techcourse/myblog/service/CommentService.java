package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequest;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.exception.CommentAuthenticationException;
import techcourse.myblog.exception.CommentException;
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
    public void addComment(CommentRequest commentRequest, UserResponse userResponse, Long articleId) {
        String contents = commentRequest.getContents();
        User commenter = userService.getUserByEmail(userResponse);
        Article article = findArticleByArticleId(articleId);

        Comment comment = commentRepository.save(new Comment(contents, commenter, article));
        article.addComment(comment);
    }

    private Article findArticleByArticleId(Long articleId) {
        return articleService.findArticle(articleId);
    }

    @Transactional
    public void update(Long commentId, CommentRequest commentRequest) {
        Comment comment = getCommentFindById(commentId);
        comment.update(commentRequest.getContents());
    }

    private Comment getCommentFindById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentException::new);
    }

    public void remove(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    public void checkAuthentication(UserResponse userResponse, Long commentId) {
        String userEmail = userResponse.getEmail();
        Comment comment = getCommentFindById(commentId);
        String commenterEmail = comment.getCommenter().getEmail();
        log.debug("userEmail : {}, commenterEmail : {} ", userEmail, commenterEmail);

        if (!userEmail.equals(commenterEmail)) {
            throw new CommentAuthenticationException();
        }
    }
}
