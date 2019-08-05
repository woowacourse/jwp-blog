package techcourse.myblog.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.CommentReadService;
import techcourse.myblog.application.CommentWriteService;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.presentation.support.LoginUser;

@RestController
@RequestMapping("/articles/{articleId}/comment")
public class CommentController {
    private static final String DELETE_SUCCESS_MESSAGE = "삭제가 완료되었습니다.";

    private final CommentReadService commentReadService;
    private final CommentWriteService commentWriteService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentReadService commentReadService, CommentWriteService commentWriteService, ArticleReadService articleReadService) {
        this.commentReadService = commentReadService;
        this.commentWriteService = commentWriteService;
        this.articleReadService = articleReadService;
    }

    @PostMapping
    public Comment createComment(@PathVariable Long articleId, @RequestBody CommentDto commentDto, LoginUser loginUser) {
        Article article = articleReadService.findById(articleId);
        return commentWriteService.save(commentDto.toComment(loginUser.getUser(), article));
    }

    @PutMapping("/{commentId}")
    public Comment updateComment(@PathVariable Long commentId, @PathVariable Long articleId, @RequestBody CommentDto commentDto, LoginUser loginUser) {
        Article article = articleReadService.findById(articleId);
        return commentWriteService.modify(commentId, commentDto.toComment(loginUser.getUser(), article));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable Long commentId, LoginUser loginUser) {
        commentReadService.findById(commentId).validateAuthor(loginUser.getUser());
        commentWriteService.deleteById(commentId);
        return new ResponseEntity<>(DELETE_SUCCESS_MESSAGE, HttpStatus.OK);
    }
}
