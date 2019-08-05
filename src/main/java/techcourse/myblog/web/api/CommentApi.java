package techcourse.myblog.web.api;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techcourse.myblog.service.CommentService;

@RestController
@RequestMapping("/api/articles/{articleId}/comments")
public class CommentApi {
    private final CommentService commentService;

    public CommentApi(CommentService commentService) {
        this.commentService = commentService;
    }
}
