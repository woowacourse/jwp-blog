package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.dto.CommentDto;
import techcourse.myblog.application.service.CommentService;
import techcourse.myblog.domain.User;
import techcourse.myblog.presentation.response.StandardResponse;

import javax.servlet.http.HttpSession;

@RestController
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public ResponseEntity<StandardResponse> addComment(@RequestBody CommentDto commentDto, @PathVariable Long articleId, HttpSession session) {
        CommentDto saved = commentService.save(commentDto, articleId, (User) session.getAttribute("user"));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(StandardResponse.success("comment is created successfully.", saved));
    }

    @DeleteMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<StandardResponse> deleteComment(@PathVariable Long commentId, HttpSession session) {
        commentService.delete(commentId, (User) session.getAttribute("user"));
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(StandardResponse.success("comment is deleted successfully", null));
    }

    @PutMapping("/articles/{articleId}/comments/{commentId}")
    public ResponseEntity<StandardResponse> updateComment(@RequestBody CommentDto commentDto, @PathVariable("commentId") Long commentId, HttpSession session) {
        CommentDto updated = commentService.modify(commentId, commentDto, (User) session.getAttribute("user"));
        return ResponseEntity.ok(StandardResponse.success("comment is updated successfully", updated));
    }
}