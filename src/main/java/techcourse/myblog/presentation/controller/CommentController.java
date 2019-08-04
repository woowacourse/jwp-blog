package techcourse.myblog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.CommentReadService;
import techcourse.myblog.application.CommentWriteService;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.presentation.support.LoginUser;

@Controller
@RequestMapping("/articles/{articleId}/comment")
public class CommentController {
    private final CommentReadService commentReadService;
    private final CommentWriteService commentWriteService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentReadService commentReadService, CommentWriteService commentWriteService, ArticleReadService articleReadService) {
        this.commentReadService = commentReadService;
        this.commentWriteService = commentWriteService;
        this.articleReadService = articleReadService;
    }

    @PostMapping
    public RedirectView createComment(@PathVariable Long articleId, CommentDto commentDto, LoginUser loginUser) {
        Article article =  articleReadService.findById(articleId);
        commentWriteService.save(commentDto.toComment(loginUser.getUser(), article));
        return new RedirectView("/articles/" + articleId);
    }

    @PutMapping("/{commentId}")
    public RedirectView updateComment(@PathVariable Long commentId, @PathVariable Long articleId, CommentDto commentDto, LoginUser loginUser) {
        Article article = articleReadService.findById(articleId);
        commentWriteService.modify(commentId, commentDto.toComment(loginUser.getUser(), article));
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView removeComment(@PathVariable Long commentId, @PathVariable Long articleId, LoginUser loginUser) {
        commentReadService.findByIdAndWriter(commentId, loginUser.getUser());
        commentWriteService.deleteById(commentId);
        return new RedirectView("/articles/" + articleId);
    }
}
