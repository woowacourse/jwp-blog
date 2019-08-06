package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.Article;
import techcourse.myblog.comment.Comment;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpSession;
import java.awt.*;
import java.util.List;

@RequestMapping("/articles/{articleId}/comment")
@RestController
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private CommentService commentService;
    private ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    public List<Comment> createComment(@PathVariable Long articleId, @RequestBody CommentDto commentDto, HttpSession httpSession) {
        log.info("아티클 아이디{}",articleId);
        User author = (User) httpSession.getAttribute("user");
        Comment comment = commentService.createComment(commentDto, author);
        articleService.addComment(articleId, comment);
        return articleService.findAllComments(articleId);
    }

//    @PutMapping("/{commentId}")
//    public String updateComment(@PathVariable Long articleId, @PathVariable Long commentId, CommentDto commentDto, HttpSession httpSession) {
//        User user = (User) httpSession.getAttribute("user");
//        commentService.updateComment(commentId, user, commentDto);
//        return "redirect:/articles/" + articleId;
//    }

    @PutMapping("/{commentId}")
    public List<Comment> updateComment(@PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentDto commentDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        commentService.updateComment(commentId, user, commentDto);
        return articleService.findAllComments(articleId);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        commentService.deleteComment(commentId, user);
        return "redirect:/articles/" + articleId;
    }

}
