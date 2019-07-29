package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.dto.CommentDto;

import javax.servlet.http.HttpSession;

@Controller
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping("/articles/{articleId}/comment")
    public String create(@PathVariable Long articleId, CommentDto commentDto, HttpSession httpSession) {
        if (checkSession(httpSession)) return "redirect:/login";
        commentDto.setWriter((User) httpSession.getAttribute("user"));
        commentDto.setArticle(articleService.findById(articleId));
        commentService.save(commentDto);
        return "redirect:/articles/" + articleId;
    }

    private boolean checkSession(HttpSession httpSession) {
        return httpSession.getAttribute("user") == null;
    }
}
