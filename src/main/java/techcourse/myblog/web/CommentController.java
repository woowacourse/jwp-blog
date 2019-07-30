package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @PutMapping("/comment/edit/{id}")
    public String editComment(@PathVariable Long id, String editedContents) {
        commentService.update(id, editedContents);

        Long articleId = commentService.findArticleIdById(id);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/comment/edit/{id}")
    public String deleteComment(@PathVariable Long id) {
        Long articleId = commentService.findArticleIdById(id);
        commentService.deleteById(id);
        return "redirect:/articles/" + articleId;
    }
}
