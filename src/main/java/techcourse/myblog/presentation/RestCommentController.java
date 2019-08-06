package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.util.List;
import java.util.stream.Collectors;

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
}
