package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentSaveRequestDto;
import techcourse.myblog.service.CommentService;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/writing")
    public String saveComment(CommentSaveRequestDto commentSaveRequestDto, User user) {
        log.info("save comment post request params={}", commentSaveRequestDto);
        commentService.save(commentSaveRequestDto, user);

        return "redirect:/articles/" + commentSaveRequestDto.getArticleId();
    }

    @PutMapping("/{id}")
    public String editComment(@PathVariable Long id, String editedContents, User user) {
        commentService.update(id, editedContents, user);
        log.info("update comment put request id={}, editedContents={}", id, editedContents);

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
