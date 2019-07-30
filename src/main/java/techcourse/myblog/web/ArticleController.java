package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CategoryService;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final CategoryService categoryService;

    @Autowired
    public ArticleController(ArticleService articleService, CategoryService categoryService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String indexView(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticleView(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "article-edit";
    }

    @PostMapping("/articles")
    public String publishArticle(ArticleRequestDto articleRequestDto) {
        articleService.addArticle(articleRequestDto);
        return "redirect:/";
    }

    @GetMapping("/articles/{articleId}")
    public String articleView(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticleView(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable Long articleId, ArticleRequestDto articleRequestDto, Model model) {
        articleService.editArticle(articleId, articleRequestDto);
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
