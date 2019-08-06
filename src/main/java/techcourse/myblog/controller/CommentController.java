package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.ResponseCommentDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.service.CommentService;

@RequestMapping("/comments")
@SessionAttributes("user")
@RestController
//@Controller
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<ResponseCommentDto> createComment(@RequestBody RequestCommentDto requestCommentDto, @ModelAttribute User user) {
        return new ResponseEntity<>(commentService.save(requestCommentDto, user), HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> updateComment(@PathVariable Long commentId,
                                                            @RequestBody RequestCommentDto requestCommentDto,
                                                            @ModelAttribute User user) {
        return new ResponseEntity<>(commentService.update(requestCommentDto, commentId, user), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> deleteComment(@PathVariable Long commentId, @ModelAttribute User user) {
        return new ResponseEntity<>(commentService.delete(commentId, user), HttpStatus.OK);
    }
}
