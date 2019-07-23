package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticlePagingService;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticlePagingService articlePagingService;

    public ArticleController(ArticlePagingService articlePagingService) {
        this.articlePagingService = articlePagingService;
    }

    @GetMapping("/new")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto) {
        return "redirect:/articles/" + articlePagingService.create(articleDto);
    }

    @GetMapping("/{id}")
    public String showArticle(@PathVariable Long id, Model model) {
        model.addAttribute("article", articlePagingService.findById(id));
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String updateArticle(@PathVariable Long id, Model model) {
        model.addAttribute("article", articlePagingService.findById(id));
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String showUpdatedArticle(@PathVariable Long id, ArticleDto updatedArticle, Model model) {
        model.addAttribute("article", articlePagingService.update(id, updatedArticle));
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articlePagingService.delete(id);
        return "redirect:/";
    }
}
