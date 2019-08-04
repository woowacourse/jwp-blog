package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment")
    public String save(HttpSession session, CommentDto commentDto) {
        User user = (User) session.getAttribute("user");
        try {
            commentService.save(user.getId(), commentDto);
        } catch (IllegalArgumentException e) {
            return "redirect:/";
        }
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @DeleteMapping("/comment")
    public String delete(HttpSession session, CommentDto commentDto) {
        User user = (User) session.getAttribute("user");
        commentService.delete(user.getId(), commentDto);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @GetMapping("/comment/{id}/edit")
    public String showEditPage(@PathVariable long id, Model model) {
        model.addAttribute("comment", commentService.findCommentByIdOrElseThrow(id));
        return "comment-edit";
    }

    @PutMapping("/comment")
    public String update(CommentDto commentDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Comment comment = commentService.update(user, commentDto);
        return "redirect:/articles/" + comment.getArticleId();
    }

}
