package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleDto;

import javax.validation.Valid;

@Controller
public class ArticleController {
    private static final String ARTICLE_INFO = "article";
    private static final String ARTICLES_INFO = "articles";

    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute(ARTICLES_INFO, articleService.findAll());

        return "index";
    }

    @GetMapping("/writing")
    public String formArticle(Model model) {
        model.addAttribute(ARTICLE_INFO, null);

        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(@Valid ArticleDto articleDto) {
        Long articleId = articleService.post(articleDto);

        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String selectArticle(@PathVariable("articleId") long articleId, Model model) {
        ArticleDto article = articleService.findById(articleId);
        model.addAttribute(ARTICLE_INFO, article);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String edit(@PathVariable("articleId") long articleId, Model model) {
        ArticleDto article = articleService.findById(articleId);
        model.addAttribute(ARTICLE_INFO, article);

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable("articleId") long articleId, @ModelAttribute ArticleDto articleDto, Model model) {
        articleService.editArticle(articleDto, articleId);
        model.addAttribute(ARTICLE_INFO, articleDto);

        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable("articleId") long articleId) {
        articleService.deleteById(articleId);

        return "redirect:/";
    }
}
