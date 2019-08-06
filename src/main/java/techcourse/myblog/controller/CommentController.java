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

    @GetMapping("/{commentId}/edit")
    public String editCommentForm(@PathVariable Long commentId, Model model, @ModelAttribute User user) {
        Comment comment = commentService.findById(commentId, user);
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

//    @DeleteMapping("/{commentId}")
//    public String deleteComment(@PathVariable Long commentId, @ModelAttribute User user) {
//        return "redirect:/articles/" + commentService.delete(commentId, user).getId();
//    }

    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable Long commentId, RequestCommentDto requestCommentDto, @ModelAttribute User user) {
        Comment newComment = commentService.update(requestCommentDto, commentId, user);
        Article commentedArticle = newComment.getArticle();
        return "redirect:/articles/" + commentedArticle.getId();
    }

    @PostMapping
    public ResponseEntity<ResponseCommentDto> createComment(@RequestBody RequestCommentDto requestCommentDto, @ModelAttribute User user) {
        return new ResponseEntity<>(commentService.save(requestCommentDto, user), HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseCommentDto> deleteComment(@PathVariable Long commentId, @ModelAttribute User user) {
        return new ResponseEntity<>(commentService.delete(commentId, user), HttpStatus.OK);
    }
}
