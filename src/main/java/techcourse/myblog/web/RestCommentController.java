package techcourse.myblog.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentResponseDto;

import java.util.List;

@RestController
public class RestCommentController {
    private CommentService commentService;

    public RestCommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/comments/{id}")
    public List<CommentResponseDto> readComments(@PathVariable("id") Long id) {
        return commentService.findCommentsByArticleId(id);
    }
}
