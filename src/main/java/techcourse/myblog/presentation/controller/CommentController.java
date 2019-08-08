package techcourse.myblog.presentation.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentReadService commentReadService;
    private final CommentWriteService commentWriteService;
    private final ArticleReadService articleReadService;

    public CommentController(CommentReadService commentReadService, CommentWriteService commentWriteService, ArticleReadService articleReadService) {
        this.commentReadService = commentReadService;
        this.commentWriteService = commentWriteService;
        this.articleReadService = articleReadService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@PathVariable Long articleId, @RequestBody CommentDto commentDto, LoginUser loginUser) {
        log.debug("comment save request data : -> {}, {}", articleId, commentDto);
        Article article = articleReadService.findById(articleId);
        Comment comment = commentWriteService.save(commentDto.toComment(loginUser.getUser(), article));
        log.debug("comment save response data : -> {}", comment);

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable Long commentId, @PathVariable Long articleId, @RequestBody CommentDto commentDto, LoginUser loginUser) {
        log.debug("comment update request data : -> {}, {}", articleId, commentDto);
        Article article = articleReadService.findById(articleId);
        Comment comment = commentWriteService.modify(commentId, commentDto.toComment(loginUser.getUser(), article));
        log.debug("comment update response data : -> {}", comment);

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> removeComment(@PathVariable Long commentId, LoginUser loginUser) {
        log.debug("comment remove request data : -> {}", commentId);
        commentReadService.findById(commentId).validateAuthor(loginUser.getUser());
        commentWriteService.deleteById(commentId);

        return new ResponseEntity<>(DELETE_SUCCESS_MESSAGE, HttpStatus.OK);
    }
}
