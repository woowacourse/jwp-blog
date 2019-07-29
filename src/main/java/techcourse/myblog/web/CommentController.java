package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

@Controller
public class CommentController {
    private ArticleService articleService;
    private CommentService commentService;

    public CommentController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

}
