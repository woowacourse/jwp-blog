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
        log.info("save comment post request params={}", commentSaveRequestDto);
        commentService.save(commentSaveRequestDto, (User) httpSession.getAttribute(USER));

        return "redirect:/articles/" + commentSaveRequestDto.getArticleId();
    }

    @PutMapping("/{id}")
    public String editComment(@PathVariable Long id, String editedContents) {
        log.info("edit comment put request id={}, editedContents={}", id, editedContents);
        commentService.update(id, editedContents);

        Long articleId = commentService.findArticleIdById(id);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{id}")
    public String deleteComment(@PathVariable Long id) {
        log.info("delete comment delete request id={}", id);
        Long articleId = commentService.findArticleIdById(id);
        commentService.deleteById(id);
        return "redirect:/articles/" + articleId;
    }
}
