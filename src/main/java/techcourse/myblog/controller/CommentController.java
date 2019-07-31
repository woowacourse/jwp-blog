package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
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

    @PostMapping
    public String save(CommentDto commentDto, User user) {
        log.debug(">>> post commentDto : {}, user : {}", commentDto, user);
        Article article = commentService.findArticleByArticleId(commentDto.getArticleId());
        Comment comment = commentDto.toComment(user, article);
        commentService.saveComment(comment);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @DeleteMapping("{id}")
    public String delete(@PathVariable long id, User user) {
        log.debug(">>> delete id : {}", id);
        Comment comment = commentService.findCommentById(id);
        if (user.isNotMatch(comment.getUser())) {
            return "redirect:/";
        }
        commentService.deleteComment(comment);
        return "redirect:/articles/" + comment.getArticle().getId();
    }

    @GetMapping("{id}/edit")
    public String showEditPage(@PathVariable long id, Model model, User user) {
        Comment comment = commentService.findCommentById(id);
        if (user.isNotMatch(comment.getUser())) {
            return "redirect:/";
        }
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PutMapping
    public String update(CommentDto commentDto, User user) {
        log.debug(">>> update commentDto : {}", commentDto);
        Comment comment = commentService.findCommentById(commentDto.getId());
        if (user.isNotMatch(comment.getUser())) {
            return "redirect:/";
        }
        Article article = commentService.findArticleByArticleId(commentDto.getArticleId());
        comment = commentDto.toComment(user, article);
        commentService.saveComment(comment);
        return "redirect:/articles/" + article.getId();
    }

}
