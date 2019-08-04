package techcourse.myblog.article.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.domain.Article;
import techcourse.myblog.article.service.ArticleService;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.utils.model.ModelUtil;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.utils.session.SessionContext.USER;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ModelUtil.addAttribute(model, "articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.findArticle(articleId));
        ModelUtil.addAttribute(model, "comments", articleService.getCommentsByArticleId(articleId));

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.findArticle(articleId));

        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        Article article = articleService.save(articleRequestDto, userResponseDto);
        return "redirect:/articles/" + article.getId();
    }

    @PutMapping("/articles/{articleId}")
    public String modifyArticle(@PathVariable long articleId, ArticleRequestDto articleRequestDto, UserResponseDto userResponseDto) {
        articleService.update(articleId, articleRequestDto, userResponseDto);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId, UserResponseDto userResponseDto) {
        articleService.delete(articleId, userResponseDto);

        return "redirect:/";
    }
}