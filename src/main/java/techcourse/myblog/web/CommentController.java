package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.domain.dto.UserDto;
import techcourse.myblog.domain.dto.response.LoginUser;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.domain.dto.CommentDto;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.web.supports.UserSession;

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
    public List<CommentDto> createComment(@UserSession LoginUser loginUser, @PathVariable Long articleId, @RequestBody CommentDto commentDto) {
        Comment comment = commentService.createComment(commentDto, loginUser);
        articleService.addComment(articleId, comment);
        return articleService.findAllComments(articleId);
    }

    @PutMapping("/{commentId}")
    public List<CommentDto> updateComment(@UserSession LoginUser loginUser, @PathVariable Long articleId, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        commentService.updateComment(commentId, loginUser, commentDto);
        return articleService.findAllComments(articleId);
    }

    @DeleteMapping("/{commentId}")
    public List<CommentDto> deleteComment(@UserSession LoginUser loginUser, @PathVariable Long articleId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId, loginUser);
        return articleService.findAllComments(articleId);
    }
}
