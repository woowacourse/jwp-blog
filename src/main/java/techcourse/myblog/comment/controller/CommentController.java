package techcourse.myblog.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.comment.dto.CommentDto;
import techcourse.myblog.comment.service.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comments/{articleId}")
    public String createComment(@PathVariable long articleId, UserSession userSession, CommentDto.Creation commentDto) {
        commentService.save(articleId, userSession.getId(), commentDto);
        return "/articles/" + articleId;
    }
}
