package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.annotation.EmailAnnot;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.CommentService;
import techcourse.myblog.domain.Email;

@RestController
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public CommentDto saveComment(@RequestBody CommentDto requestCommentDto, @EmailAnnot Email email) {
        CommentDto commentDto = commentService.saveComment(requestCommentDto, email.getEmail());
        return commentDto;
    }

    @PutMapping("/articles/{articleId}/comments/{updateCommentId}")
    public CommentDto updateComment(@RequestBody CommentDto requestCommentDto) {
        return commentService.update(requestCommentDto);
    }

    @DeleteMapping("/articles/{articleId}/comments/{deleteCommentId}")
    public ResponseEntity deleteComment(@PathVariable("deleteCommentId") long commentId) {
        commentService.delete(commentId);
        return new ResponseEntity(HttpStatus.OK);
    }
}
