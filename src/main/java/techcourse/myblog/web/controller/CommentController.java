package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.domain.service.ArticleService;
import techcourse.myblog.domain.service.CommentService;
import techcourse.myblog.dto.CommentDto;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;

    @Autowired
    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping("")
    public RedirectView createComment(CommentDto commentDto, HttpSession httpSession) {
        Comment comment = convert(commentDto, httpSession);
        commentService.save(comment);
        return new RedirectView("/articles/" + commentDto.getArticleId());
    }

    @PutMapping("/{commentId}")
    public RedirectView updateComment(@PathVariable long articleId, @PathVariable long commentId, String contents, HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute("user");
        commentService.update(commentId, loginUser, contents);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{commentId}")
    public RedirectView deleteComment(@PathVariable long commentId, @PathVariable long articleId, HttpSession httpSession) {
        User loginUser = (User) httpSession.getAttribute("user");
        commentService.deleteById(commentId, loginUser.getId());
        return new RedirectView("/articles/" + articleId);
    }

    private Comment convert(CommentDto commentDto, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute("user");
        Article article = articleService.findById(commentDto.getArticleId());
        return new Comment(commentDto.getContents(), author, article);
    }
}
