package techcourse.myblog.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class RestArticleCommentController {
    private CommentService commentService;

    public RestArticleCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}/comments")
    public List<CommentResponseDto> readArticleComments(@PathVariable("articleId") Long id) {
        return commentService.findCommentsByArticleId(id);
    }
}
