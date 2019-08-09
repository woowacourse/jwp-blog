package techcourse.myblog.web.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.CommentDto;
import techcourse.myblog.web.UserSession;

@Deprecated
@Controller
@RequestMapping("/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private static final Logger log = LoggerFactory.getLogger(CommentController.class);

    private final CommentService commentService;

    @PostMapping
    public String create(CommentDto.Create commentDto, UserSession userSession) {
        log.debug("contents: {}", commentDto.getContents());

        commentService.save(commentDto, userSession.getId());

        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @PutMapping("/{id}")
    public String edit(CommentDto.Update commentDto, UserSession userSession) {
        commentService.update(commentDto, userSession.getId());

        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long articleId,
                         @PathVariable Long id,
                         UserSession userSession) {

        final Long userId = userSession.getId();
        commentService.delete(id, userId);

        return "redirect:/articles/" + articleId;
    }

}
