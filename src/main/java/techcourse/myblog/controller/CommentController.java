package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.CommentService;

import static techcourse.myblog.controller.CommentController.COMMENT_URL;

@Slf4j
@Controller
@RequestMapping(COMMENT_URL)
public class CommentController {
    private final CommentService commentService;
    public static final String COMMENT_URL = "/comment";

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("{id}/edit")
    public String showEditPage(@PathVariable long id, Model model, User user) {
        Comment comment;
        try {
            comment = commentService.find(id, user);
        } catch (RuntimeException e) {
            return "redirect:/";
        }
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PostMapping
    public String save(CommentDto commentDto, User user) {
        log.debug(">>> post commentDto : {}, user : {}", commentDto, user);
        commentService.save(commentDto, user);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @PutMapping("{id}")
    public String update(@PathVariable long id, CommentDto commentDto, User user) {
        log.debug(">>> update commentDto : {}", commentDto);
        if (commentService.isSuccessUpdate(id, commentDto, user)) {
            return "redirect:/articles/" + commentDto.getArticleId();
        }

        return "redirect:/";
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable long id, User user) {
        log.debug(">>> delete id : {}", id);
        long articleId;
        try {
            articleId = commentService.deleteAndReturnArticleId(id, user);
        } catch (RuntimeException e) {
            return "redirect:/";
        }
        return "redirect:/articles/" + articleId;
    }

}
