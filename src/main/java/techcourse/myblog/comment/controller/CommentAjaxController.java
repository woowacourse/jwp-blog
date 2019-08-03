package techcourse.myblog.comment.controller;

import org.springframework.web.bind.annotation.*;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.comment.dto.CommentAjaxResponseDto;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentUpdateDto;
import techcourse.myblog.comment.service.CommentService;

import java.util.List;

@RestController
public class CommentAjaxController {
    private CommentService commentService;

    public CommentAjaxController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/ajax/articles/{articleId}/comments")
    public List<CommentAjaxResponseDto> add(@RequestBody CommentCreateDto commentCreateDto, @PathVariable long articleId, UserSession userSession) {
        commentService.save(articleId, userSession.getId(), commentCreateDto);
        return commentService.findAllByArticleIdTwo(articleId);
    }

    @PutMapping("/ajax/articles/{articleId}/comments/{commentId}")
    public List<CommentAjaxResponseDto> update(@RequestBody CommentUpdateDto commentUpdateDto, @PathVariable long articleId, @PathVariable long commentId, UserSession userSession) {
        commentService.update(commentId, userSession.getId(), commentUpdateDto);
        return commentService.findAllByArticleIdTwo(articleId);
    }

    @DeleteMapping("/ajax/articles/{articleId}/comments/{commentId}")
    public List<CommentAjaxResponseDto> delete(@PathVariable long articleId, @PathVariable long commentId, UserSession userSession) {
        commentService.deleteById(commentId, userSession.getId());
        return commentService.findAllByArticleIdTwo(articleId);
    }
}
