package techcourse.myblog.comments;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.users.UserSession;

@Controller
@RequestMapping("/comments")
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

}
