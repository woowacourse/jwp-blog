package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.SessionUser;

import java.util.List;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentService commentService, ArticleReadService articleReadService) {
        this.commentService = commentService;
        this.articleReadService = articleReadService;
    }

    @GetMapping
    public List<Comment> showComments(@PathVariable Long articleId) {
        return commentService.findByArticleId(articleId);
    }

    @GetMapping("/count")
    public long countComments(@PathVariable Long articleId) {
        return commentService.countByArticleId(articleId);
    }

    @PostMapping
    public Comment createComment(SessionUser loginUser, @PathVariable Long articleId, @RequestBody CommentDto commentDto) {
        log.info("Comment create: contents={}", commentDto.getContents());

        Article article = articleReadService.findById(articleId);
        Comment comment = commentService.save(commentDto.toComment(loginUser.getUser(), article));

        return comment;
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(SessionUser loginUser, @PathVariable Long commentId, @RequestBody CommentDto commentDto) {
        log.info("Comment update: id={}, contents={}", commentId, commentDto.getContents());

        Comment updatedComment = commentService.modify(commentId, commentDto, loginUser.getUser());

        return updatedComment;
    }

    @DeleteMapping("/{commentId}")
    public Long removeComment(@PathVariable Long commentId, SessionUser loginUser) {
        Long deletedId = commentService.deleteById(commentId, loginUser.getUser());

        return deletedId;
    }
}
