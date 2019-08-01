package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.InvalidAuthorException;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showIndex(Model model) {
        model.addAttribute("articleDtos", articleService.getAllArticles());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showWritingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(ArticleDto articleDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        long articleId = articleService.save(articleDto, user).getArticleId();
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        ArticleDto articleDto = articleService.getArticleDtoById(articleId);
        model.addAttribute("articleDto", articleDto);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showEditForm(@PathVariable long articleId, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Article article = articleService.getArticleById(articleId);
        article.checkCorrespondingAuthor(user);
        ArticleDto articleDto = articleService.getArticleDtoById(articleId);
        model.addAttribute("articleDto", articleDto);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto articleDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.update(articleDto, user);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Article article = articleService.getArticleById(articleId);
        article.checkCorrespondingAuthor(user);
        articleService.deleteById(articleId);
        return "redirect:/";
    }

    @ExceptionHandler(InvalidAuthorException.class)
    public String handleInvalidAuthorException(InvalidAuthorException e) {
        return "redirect:/";
    }
}
