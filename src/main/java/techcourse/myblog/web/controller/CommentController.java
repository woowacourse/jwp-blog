package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.LoginUser;

import javax.validation.Valid;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentService commentService, ArticleReadService articleReadService) {
        this.commentService = commentService;
        this.articleReadService = articleReadService;
    }

    @PostMapping("/articles/{articleId}/comment")
    public RedirectView createComment(LoginUser loginUser, @PathVariable Long articleId, @Valid CommentDto commentDto) {
        commentDto.setArticle(articleReadService.findById(articleId));
        commentDto.setWriter(loginUser.getUser());
        commentService.save(commentDto);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public RedirectView removeComment(@PathVariable Long commentId, @PathVariable Long articleId, LoginUser loginUser) {
        commentService.findByIdAndWriter(commentId, loginUser.getUser());
        commentService.deleteById(commentId);
        return new RedirectView("/articles/" + articleId);
    }
}
