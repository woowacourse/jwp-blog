package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/comment/writing")
    public String saveComment(CommentSaveRequestDto commentSaveRequestDto, HttpSession httpSession) {
        commentService.save(commentSaveRequestDto, (User) httpSession.getAttribute(USER));

        return "redirect:/articles/" + commentSaveRequestDto.getArticleId();
    }
}
