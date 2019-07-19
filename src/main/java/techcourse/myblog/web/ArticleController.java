package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CategoryService;

import java.util.Optional;

@Controller
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/articles/new")
    public String showArticleWritingPage(Model model) {
        model.addAttribute("categories", categoryService.readAll());
        return "article-edit";
    }

    @PostMapping("/articles/new")
    public String addArticle(ArticleDto articleDto) {
        long articleId = articleService.createArticle(articleDto);

        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable long articleId, Model model) {
        Optional<ArticleDto> maybeArticleDto = articleService.readById(articleId);

        if (maybeArticleDto.isPresent()) {
            model.addAttribute("article", maybeArticleDto.get());
            return "article";
        }
        return "error";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto articleDto) {
        ArticleDto toArticleDto = articleService.updateByArticle(articleId, articleDto);

        return "redirect:/articles/" + toArticleDto.getId();
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable long articleId, Model model) {
        Optional<ArticleDto> maybeArticleDto = articleService.readById(articleId);

        if (maybeArticleDto.isPresent()) {
            model.addAttribute("article", maybeArticleDto.get());
            model.addAttribute("categories", categoryService.readAll());
            return "article-edit";
        }
        return "error";
    }

    @DeleteMapping("articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
