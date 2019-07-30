package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.util.SessionKeys.USER;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/writing")
    public String saveComment(CommentSaveRequestDto commentSaveRequestDto, HttpSession httpSession) {
        commentService.save(commentSaveRequestDto, (User) httpSession.getAttribute(USER));

        return "redirect:/articles/" + commentSaveRequestDto.getArticleId();
    }

    @PutMapping("/edit/{id}")
    public String editComment(@PathVariable Long id, String editedContents) {
        commentService.update(id, editedContents);

        Long articleId = commentService.findArticleIdById(id);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/edit/{id}")
    public String deleteComment(@PathVariable Long id) {
        Long articleId = commentService.findArticleIdById(id);
        commentService.deleteById(id);
        return "redirect:/articles/" + articleId;
    }
}
