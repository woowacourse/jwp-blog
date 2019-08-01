package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.LoginUser;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentService commentService, ArticleReadService articleReadService) {
        this.commentService = commentService;
        this.articleReadService = articleReadService;
    }

    @PostMapping
    public RedirectView createComment(LoginUser loginUser, @PathVariable Long articleId, CommentDto commentDto) {
        commentDto.setArticle(articleReadService.findById(articleId));
        commentDto.setWriter(loginUser.getUser());
        commentService.save(commentDto);

        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/{commentId}")
    public RedirectView updateComment(LoginUser loginUser, @PathVariable Long commentId, @PathVariable Long articleId, CommentDto commentDto) {
        commentDto.setArticle(articleReadService.findById(articleId));
        commentDto.setWriter(loginUser.getUser());
        commentService.modify(commentId, commentDto);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView removeComment(@PathVariable Long commentId, @PathVariable Long articleId, LoginUser loginUser) {
        commentService.findByIdAndWriter(commentId, loginUser.getUser());
        commentService.deleteById(commentId);
        return new RedirectView("/articles/" + articleId);
    }
}
