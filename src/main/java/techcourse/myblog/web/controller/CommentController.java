package techcourse.myblog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.Comments;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.annotation.LoginUser;

@Slf4j
@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<Comments> getComments(@PathVariable long articleId) {
        log.info("articleId : {}", articleId);
        Comments comments = commentService.findByArticle(articleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto, @PathVariable long articleId, @LoginUser User loginUser) {
        log.info("articleId : {}", articleId);
        log.info("loginUser : {}", loginUser);
        log.info("CommentDto : {}", commentDto);
        Comment savedComment = commentService.save(commentDto, loginUser, articleId);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{commentId}")
    public ResponseEntity<Comment> updateComment(@PathVariable long commentId, @LoginUser User loginUser, @RequestBody CommentDto commentDto) {
        Comment updatedComment = commentService.update(commentId, loginUser, commentDto.getContents());
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Comment> deleteComment(@PathVariable long commentId, @LoginUser User loginUser) {
        commentService.deleteById(commentId, loginUser.getId());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
