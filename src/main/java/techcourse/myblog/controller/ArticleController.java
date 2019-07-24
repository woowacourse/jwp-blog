package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

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

    @GetMapping("/writing")
    public String showWritingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(ArticleDto articleDto) {
        long articleId = articleService.save(articleDto);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        ArticleDto articleDto = articleService.getArticleDtoById(articleId);
        model.addAttribute("articleDto", articleDto);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showEditForm(@PathVariable long articleId, Model model) {
        ArticleDto articleDto = articleService.getArticleDtoById(articleId);
        model.addAttribute("articleDto", articleDto);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto articleDto) {
        articleService.update(articleDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.deleteArticleById(articleId);
        return "redirect:/";
    }
}
