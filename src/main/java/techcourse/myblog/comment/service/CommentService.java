package techcourse.myblog.comment.service;

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
    private final UserService userService;
    private final ArticleService articleService;
    private final CommentRepository commentRepository;

    public CommentService(UserService userService, ArticleService articleService, CommentRepository commentRepository) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentRepository = commentRepository;
    }

    public void addComment(CommentRequestDto commentRequestDto, UserResponseDto userResponseDto, Long articleId) {
        String contents = commentRequestDto.getContents();
        User commenter = userService.getUserByEmail(userResponseDto.getEmail());
        Article article = articleService.findArticle(articleId);

        article.addComments(new Comment(contents, commenter, article));
    }

    public void update(Long commentId, CommentRequestDto commentRequestDto, UserResponseDto userResponseDto) {
        Comment comment = getCommentById(commentId);
        comment.update(commentRequestDto, userService.getUserByEmail(userResponseDto.getEmail()));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
    }

    public void remove(Long commentId) {
        Comment comment = getCommentById(commentId);
        commentRepository.delete(comment);
    }

    public void checkAuthentication(Long commentId, UserResponseDto userResponseDto) {
        Comment comment = getCommentById(commentId);
        User user = userService.getUserByEmail(userResponseDto.getEmail());
        System.out.println(comment.getCommenter().getEmail());
        if (!comment.isCommenter(user)) {
            throw new CommentAuthenticationException();
        }
    }
}
