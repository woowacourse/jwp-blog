package techcourse.myblog.comment.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.comment.domain.Comment;
import techcourse.myblog.comment.exception.CommentAuthenticationException;
import techcourse.myblog.comment.exception.NotFoundCommentException;
import techcourse.myblog.comment.repository.CommentRepository;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.user.domain.User;
import techcourse.myblog.user.service.UserService;

@Service
@Transactional
public class CommentService {
    private static final Logger log = LoggerFactory.getLogger(CommentService.class);
    private static final String LOG_TAG = "[CommentService]";

    private final UserService userService;
    private final ArticleService articleService;
    private final CommentRepository commentRepository;

    public CommentService(UserService userService, ArticleService articleService, CommentRepository commentRepository) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentRepository = commentRepository;
    }

    public Comment addComment(CommentRequestDto commentRequestDto, UserResponseDto userResponseDto, Long articleId) {
        String contents = commentRequestDto.getContents();
        User commenter = userService.getUserByEmail(userResponseDto.getEmail());
        Article article = articleService.findArticle(articleId);

        Comment comment = new Comment(contents, commenter, article);
        article.addComments(comment);
        return comment;
    }

    public void update(Long commentId, CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = getCommentById(commentId);

        checkAuthentication(commentId, userResponseDto);
        comment.update(commentRequestDto, userService.getUserByEmail(userResponseDto.getEmail()));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
    }

    public void remove(Long commentId, UserResponseDto userResponseDto) {
        Comment comment = getCommentById(commentId);

        checkAuthentication(commentId, userResponseDto);
        commentRepository.delete(comment);
    }

    public void checkAuthentication(Long commentId, UserResponseDto userResponseDto) {
        User user = userService.getUserByEmail(userResponseDto.getEmail());
        Comment comment = getCommentById(commentId);
        log.debug("{} comment.getCommenter().getEmail() >> {}", LOG_TAG, comment.getCommenter().getEmail());

        if (!comment.isCommenter(user)) {
            throw new CommentAuthenticationException();
        }
    }
}