package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.RequestCommentDto;
import techcourse.myblog.controller.dto.ResponseCommentDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.Comment;
import techcourse.myblog.model.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

@RequestMapping("/comments")
@SessionAttributes("user")
@RestController
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);
    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseCommentDto createComment(
            @RequestBody RequestCommentDto requestCommentDto, @ModelAttribute User user) {
        log.info("Created requestCommentDto : {}", requestCommentDto.toString());
        Article foundArticle = articleService.findById(requestCommentDto.getArticleId());
        requestCommentDto.setUser(user);
        requestCommentDto.setArticle(foundArticle);
        Comment comment = commentService.save(requestCommentDto);
        return new ResponseCommentDto(
                comment.getId(),
                comment.getContents(),
                comment.getAuthor().getUserName(),
                comment.getCurrentDateTime()
        );
    }

    @GetMapping("/{commentId}")
    public ResponseCommentDto fetchComment(@PathVariable Long commentId) {
        Comment comment = commentService.findById(commentId);
        return new ResponseCommentDto(
                comment.getId(),
                comment.getContents(),
                comment.getAuthor().getUserName(),
                comment.getCurrentDateTime()
        );
    }


    @PutMapping("/{commentId}")
    public ResponseCommentDto updateComment(
            @PathVariable Long commentId, @RequestBody RequestCommentDto requestCommentDto,
            @ModelAttribute User user) {
        commentService.checkOwner(commentId, user);
        Comment newComment = commentService.update(requestCommentDto, commentId);

        return new ResponseCommentDto(
                newComment.getId(),
                newComment.getContents(),
                newComment.getAuthor().getUserName(),
                newComment.getCurrentDateTime()
        );
    }

    @DeleteMapping("/{commentId}")
    private void deleteComment(@PathVariable Long commentId, @ModelAttribute User user) {
        commentService.checkOwner(commentId, user);
        commentService.delete(commentId);
    }
}
