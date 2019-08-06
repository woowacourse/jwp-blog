package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.annotation.LoginUser;

@RestController
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;
    private final ArticleService articleService;

    @Autowired
    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<Comment> createComment(@RequestBody CommentDto commentDto, @PathVariable long articleId, @LoginUser User loginUser) {
        Comment comment = convert(commentDto, loginUser, articleId);
        Comment savedComment = commentService.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.OK);
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

    private Comment convert(CommentDto commentDto, @LoginUser User loginUser, long articleId) {
        Article article = articleService.findById(articleId);
        return new Comment(commentDto.getContents(), loginUser, article);
    }
}
