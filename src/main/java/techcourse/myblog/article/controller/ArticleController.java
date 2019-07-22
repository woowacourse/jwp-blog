package techcourse.myblog.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.dto.ArticleDto;
import techcourse.myblog.article.service.ArticleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage(HttpServletRequest request) {
        if (checkSession(request)) return "redirect:/login";
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Create articleDto, HttpServletRequest request) {
        if (checkSession(request)) return "redirect:/login";
        long newArticleId = articleService.save(articleDto);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, HttpServletRequest request, Model model) {
        if (checkSession(request)) return "redirect:/login";
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, HttpServletRequest request, Model model) {
        if (checkSession(request)) return "redirect:/login";
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto.Update articleDto, HttpServletRequest request) {
        if (checkSession(request)) return "redirect:/login";
        long updatedArticleId = articleService.update(articleId, articleDto);
        return "redirect:/articles/" + updatedArticleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId, HttpServletRequest request) {
        if (checkSession(request)) return "redirect:/login";
        articleService.deleteById(articleId);
        return "redirect:/";
    }

    private boolean checkSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return session == null;
    }
}
