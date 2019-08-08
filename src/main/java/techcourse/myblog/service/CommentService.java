package techcourse.myblog.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentRequestDto;
import techcourse.myblog.dto.CommentResponseDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.exception.CommentAuthenticationException;
import techcourse.myblog.exception.NotFoundCommentException;
import techcourse.myblog.repository.CommentRepository;
import techcourse.myblog.utils.converter.CommentConverter;

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

    public CommentResponseDto addComment(CommentRequestDto commentRequestDto, UserResponseDto userResponseDto, Long articleId) {
        String contents = commentRequestDto.getContents();
        User commenter = userService.getUserByEmail(userResponseDto.getEmail());
        Article article = articleService.findArticle(articleId);

        Comment comment = new Comment(contents, commenter, article);
        article.addComments(comment);

        return CommentConverter.toResponseDto(comment);
    }

    public CommentResponseDto update(Long commentId, CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = getCommentById(commentId);

        checkAuthentication(commentId, userResponseDto);
        comment.update(commentRequestDto, userService.getUserByEmail(userResponseDto.getEmail()));

        return CommentConverter.toResponseDto(comment);
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