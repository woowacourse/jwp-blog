package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

import static techcourse.myblog.service.UserService.LOGGED_IN_USER_SESSION_KEY;

@Slf4j
@Controller
public class RestCommentController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public RestCommentController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponseDto>> getComments(@PathVariable long articleId) {
        return new ResponseEntity<>(commentService.findByArticle(articleService.findById(articleId))
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @PostMapping("/articles/{articleId}/comments/rest")
    public ResponseEntity<List<CommentResponseDto>> addNewComment2(@PathVariable long articleId, @RequestBody CommentRequestDto commentRequestDto,
                                                                   HttpSession httpSession) {
        Article article = articleService.findById(articleId);
        User commenter = (User) httpSession.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        commentService.save(commentRequestDto.toComment(commenter, article));

        return new ResponseEntity<>(commentService.findByArticle(article).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }

    @PutMapping("/comments/{commentId}/rest")
    public ResponseEntity<List<CommentResponseDto>> processUpdateCommentRest(@PathVariable long commentId, @RequestBody CommentRequestDto commentRequestDto) {
        Comment comment = commentService.findById(commentId);
        commentService.update(commentId, commentRequestDto);
        return new ResponseEntity<>(commentService.findByArticle(comment.getArticle()).stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList()), HttpStatus.ACCEPTED);
    }
}
