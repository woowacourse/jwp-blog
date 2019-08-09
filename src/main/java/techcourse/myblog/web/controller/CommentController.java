package techcourse.myblog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.Comments;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.annotation.LoginUser;

@Slf4j
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

    @GetMapping
    public ResponseEntity<Comments> getComments(@PathVariable long articleId) {
        log.info("articleId : {}", articleId);
        Article article = articleService.findById(articleId);
        Comments comments = commentService.findByArticle(article);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto, @PathVariable long articleId, @LoginUser User loginUser) {
        log.info("articleId : {}", articleId);
        log.info("loginUser : {}", loginUser);
        log.info("CommentDto : {}", commentDto);
        Comment comment = convert(commentDto, loginUser, articleId);
        CommentDto savedComment = commentService.save(comment);
        return new ResponseEntity<>(savedComment, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable long commentId, @LoginUser User loginUser, @RequestBody CommentDto commentDto) {
        CommentDto updatedComment = commentService.update(commentId, loginUser, commentDto.getContents());
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
