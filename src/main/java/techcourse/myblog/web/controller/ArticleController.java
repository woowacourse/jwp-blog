package techcourse.myblog.web.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.web.UserSession;

import java.util.List;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/new")
    public String writeForm() {
        return "article-edit";
    }

    @PostMapping
    public String write(UserSession userSession, ArticleDto.Request articleDto) {
        Long userId = userSession.getId();
        Long articleId = articleService.save(userId, articleDto);

        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        final ArticleDto.Response articleDto = articleService.getOne(id);
        final List<Comment> comments = articleDto.getComments();

        model.addAttribute("article", articleDto);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, UserSession userSession, Model model) {
        Long userId = userSession.getId();
        ArticleDto.Response articleDto = articleService.getOne(userId, id);

        model.addAttribute("article", articleDto);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String edit(UserSession userSession, ArticleDto.Request editedArticle) {
        Long userId = userSession.getId();
        Long articleId = articleService.edit(userId, editedArticle);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, UserSession userSession) {
        Long userId = userSession.getId();
        articleService.deleteById(userId, id);

        return "redirect:/";
    }
}
