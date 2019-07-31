package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.CommentDto;
import techcourse.myblog.domain.*;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.controller.CommentController.COMMENT_URL;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping(COMMENT_URL)
public class CommentController {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    public static final String COMMENT_URL = "/comment";

    @PostMapping
    public String save(CommentDto commentDto, User user) {
        log.debug(">>> post commentDto : {}, user : {}", commentDto, user);
        Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(user, article);
        commentRepository.save(comment);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @DeleteMapping
    public String delete(CommentDto commentDto, User user) {
        log.debug(">>> delete commentDto : {}", commentDto);
        Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(user, article);
        commentRepository.delete(comment);
        return "redirect:/articles/" + commentDto.getArticleId();
    }

    @GetMapping("{id}/edit")
    public String showEditPage(@PathVariable long id, Model model) {
        Comment comment = commentRepository.findById(id).get();
        model.addAttribute("comment", comment);
        return "comment-edit";
    }

    @PutMapping
    public String update(CommentDto commentDto, User user) {
        log.debug(">>> update commentDto : {}", commentDto);
        Article article = articleRepository.findById(commentDto.getArticleId()).get();
        Comment comment = commentDto.toComment(user, article);
        commentRepository.save(comment);
        return "redirect:/articles/" + article.getId();
    }

}
