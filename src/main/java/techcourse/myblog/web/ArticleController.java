package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.ArticleException;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.utils.ModelUtil;

@Controller
public class ArticleController {
    private ArticleService articleService;

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
        try {
            ModelUtil.addAttribute(model, "article", articleService.findArticle(articleId));
            return "article";
        } catch (ArticleException e) {
            return "/";
        }
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.findArticle(articleId));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(ArticleRequestDto articleRequestDto) {
        return "redirect:/articles/" + articleService.save(articleRequestDto);
    }

    @PutMapping("/articles/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId, ArticleRequestDto articleRequestDto, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.update(articleId, articleRequestDto));
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return "redirect:/";
    }
}