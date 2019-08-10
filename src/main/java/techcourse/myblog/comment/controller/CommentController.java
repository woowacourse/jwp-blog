package techcourse.myblog.comment.controller;

import org.springframework.web.bind.annotation.*;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentResponseDto;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.comment.service.CommentService;

import java.util.List;

@RequestMapping("/articles/{articleId}/comments")
@RestController
public class CommentController {
    private CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public List<CommentResponseDto> add(@RequestBody CommentCreateDto commentCreateDto, @PathVariable long articleId, UserSession userSession) {
        commentService.save(articleId, userSession.getId(), commentCreateDto);
        return commentService.findAllByArticleId(articleId);
    }

    @PutMapping("/{commentId}")
    public List<CommentResponseDto> update(@RequestBody CommentUpdateDto commentUpdateDto, @PathVariable long articleId, @PathVariable long commentId, UserSession userSession) {
        commentService.update(commentId, userSession.getId(), commentUpdateDto);
        return commentService.findAllByArticleId(articleId);
    }

    @DeleteMapping("/{commentId}")
    public List<CommentResponseDto> delete(@PathVariable long articleId, @PathVariable long commentId, UserSession userSession) {
        commentService.deleteById(commentId, userSession.getId());
        return commentService.findAllByArticleId(articleId);
    }
}
