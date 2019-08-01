package techcourse.myblog.service;

import org.springframework.stereotype.Service;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.comment.CommentRepository;
import techcourse.myblog.domain.exception.UserMismatchException;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.exception.NotFoundCommentException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(NotFoundCommentException::new);
    }

    public List<CommentResponseDto> findCommentsByArticleId(long articleId) {
        Article article = articleService.findById(articleId);
        return commentRepository.findAllByArticle(article).stream()
                .map(comment -> toCommentResponseDto(comment.getId(), comment.getAuthorId(),
                        comment.getAuthorName(), comment.getComment()))
                .collect(Collectors.toList());
    }

    public Long findArticleId(long commentId) {
        Comment comment = findById(commentId);
        return comment.getArticleId();
    }

    public Comment save(Long userId, CommentRequestDto commentRequestDto) {
        User user = userService.findById(userId);
        Article article = articleService.findById(commentRequestDto.getArticleId());
        Comment comment = commentRequestDto.toEntity(user, article);
        return commentRepository.save(comment);
    }

    @Transactional
    public Comment update(Long userId, Long commentId, CommentRequestDto commentRequestDto) {
        Comment comment = findById(commentId);
        comment.updateComment(commentRequestDto.getComment(), userId);
        return comment;
    }

    public void delete(Long userId, Long commentId) {
        Comment comment = findById(commentId);
        if (!comment.matchAuthorId(userId)) {
            throw new UserMismatchException();
        }
        commentRepository.deleteById(commentId);
    }

    private CommentResponseDto toCommentResponseDto(Long commentId, Long authorId, String userName, String comment) {
        return new CommentResponseDto(commentId, authorId, userName, comment);
    }
}
