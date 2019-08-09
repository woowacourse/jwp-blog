package techcourse.myblog.application.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.application.dto.CommentRequestDto;
import techcourse.myblog.application.dto.CommentResponseDto;
import techcourse.myblog.application.service.exception.NotExistCommentException;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.CommentRepository;
import techcourse.myblog.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    @Autowired
    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    @Transactional
    public CommentResponseDto save(CommentRequestDto commentRequestDto, String userEmail, long articleId) {
        User user = userService.findUserByEmail(userEmail);
        Article article = articleService.findArticleById(articleId);

        Comment comment = new Comment(commentRequestDto.getContents(), user, article);
        return CommentResponseDto.of(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllByArticleId(Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticleId(articleId);
        return comments.stream()
                .map(CommentResponseDto::of)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto update(long commentId, CommentRequestDto commentRequestDto, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotExistCommentException("해당 댓글이 존재하지 않습니다."));

        userService.checkEmail(comment.getUser(), email);

        User user = userService.findUserByEmail(email);

        return CommentResponseDto.builder()
                .id(comment.getId())
                .contents(comment.modify(commentRequestDto.getContents(), user))
                .build();
    }

    @Transactional
    public CommentResponseDto delete(Long commentId, String email) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotExistCommentException("해당 댓글이 존재하지 않습니다."));

        userService.checkEmail(comment.getUser(), email);
        commentRepository.deleteById(commentId);

        return CommentResponseDto.builder()
                .id(commentId)
                .build();
    }
}
