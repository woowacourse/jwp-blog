package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.dto.UserResponseDto;
import techcourse.myblog.utils.model.ModelUtil;

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