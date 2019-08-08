package techcourse.myblog.comment.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.comment.dto.CommentCreateDto;
import techcourse.myblog.comment.dto.CommentResponseDto;
import techcourse.myblog.comment.service.CommentService;

@Slf4j
@RestController
public class CommentRestController {
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/articles/{articleId}/comments")
    public CommentResponseDto createComment(@PathVariable long articleId, UserSession userSession, @RequestBody CommentCreateDto commentDto) {
        log.debug(">>> commentDto : {}", commentDto);
        return commentService.save(articleId, userSession.getId(), commentDto);
    }
}
