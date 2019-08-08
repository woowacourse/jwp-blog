package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.domain.dto.CommentDto;
import techcourse.myblog.domain.user.User;

import javax.servlet.http.HttpSession;
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
    public List<CommentDto> createComment(@PathVariable Long articleId, @RequestBody CommentDto commentDto, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute("user");
        Comment comment = commentService.createComment(commentDto, author);
        articleService.addComment(articleId, comment);
        return articleService.findAllComments(articleId);
    }

    @PutMapping("/{commentId}")
    public List<CommentDto> updateComment(@PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentDto commentDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        commentService.updateComment(commentId, user, commentDto);
        return articleService.findAllComments(articleId);
    }

    @DeleteMapping("/{commentId}")
    public List<CommentDto> deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        commentService.deleteComment(commentId, user);
        return articleService.findAllComments(articleId);
    }
}
