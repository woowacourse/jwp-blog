package techcourse.myblog.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.LoginUser;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.service.dto.ResponseCommentDto;

import java.util.List;

@RequestMapping("/articles/{articleId}/comment")
@RestController
public class CommentController {
    private CommentService commentService;
    private ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<List<ResponseCommentDto>> createComment(@PathVariable Long articleId,
                                                                  @RequestBody CommentDto commentDto, @LoginUser User user) {
        Article article = articleService.findById(articleId);
        commentService.createComment(commentDto, user, article);

        return new ResponseEntity<>(commentService.findAllComments(articleId), HttpStatus.CREATED);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<List<ResponseCommentDto>> updateComment(@PathVariable Long articleId,
                                                                  @PathVariable Long commentId, @RequestBody CommentDto commentDto, @LoginUser User user) {
        commentService.updateComment(commentId, user, commentDto);

        return new ResponseEntity<>(commentService.findAllComments(articleId), HttpStatus.CREATED);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<List<ResponseCommentDto>> deleteComment(@PathVariable Long articleId,
                                                                  @PathVariable Long commentId, @LoginUser User user) {
        commentService.deleteComment(commentId, user);

        return new ResponseEntity<>(commentService.findAllComments(articleId), HttpStatus.NO_CONTENT);
    }

}
