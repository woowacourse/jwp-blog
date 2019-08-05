package techcourse.myblog.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentRequest;
import techcourse.myblog.service.dto.CommentsResponse;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@RestController
@RequestMapping("/comments")
public class CommentController {
    private CommentService commentService;
    private ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<CommentsResponse> getComment(@PathVariable("articleId") long articleId) {
        return new ResponseEntity<>(commentService.findByArticleId(articleId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Comment> saveComment(@RequestBody @Valid CommentRequest commentRequest, HttpSession httpSession) {
        // TODO: 2019-08-05 remove article service
        Article article = articleService.findById(commentRequest.getArticleId());
        Comment comment = commentService.save(commentRequest, article, (User) httpSession.getAttribute("user"));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<Comment> editComment(@RequestBody @Valid CommentRequest commentRequest, @PathVariable("commentId") Long commentId, HttpSession httpSession) {
        Comment updatedComment = commentService.update(commentRequest, (User) httpSession.getAttribute("user"), commentId);
        return new ResponseEntity<>(updatedComment, HttpStatus.OK);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable("commentId") Long commentId, HttpSession httpSession) {
        // TODO: 2019-08-03 HttpSession -> ArgumentResolver
        commentService.deleteById(commentId, (User) httpSession.getAttribute("user"));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
