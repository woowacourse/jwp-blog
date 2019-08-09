package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.SessionInfo;
import techcourse.myblog.web.support.UserSessionInfo;

@Controller
@RequestMapping("/articles/{articleId}/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public String addComment(@PathVariable long articleId,
                             @SessionInfo UserSessionInfo userSessionInfo,
                             CommentDto commentDto) {
        commentService.add(articleId, userSessionInfo.toUser(), commentDto);
        return redirectResolver(articleId);
    }

    @DeleteMapping("/{commentId}")
    public String deleteComment(@PathVariable long articleId,
                                @PathVariable long commentId,
                                @SessionInfo UserSessionInfo userSessionInfo) {
        commentService.delete(commentId, userSessionInfo.toUser());
        return redirectResolver(articleId);
    }

    @PutMapping("/{commentId}")
    public String updateComment(@PathVariable long articleId,
                                @PathVariable long commentId,
                                @SessionInfo UserSessionInfo userSessionInfo,
                                CommentDto commentDto) {
        commentService.update(commentId, userSessionInfo.toUser(), commentDto);
        return redirectResolver(articleId);
    }

    private String redirectResolver(long articleId) {
        return "redirect:/articles/" + articleId;
    }
}
