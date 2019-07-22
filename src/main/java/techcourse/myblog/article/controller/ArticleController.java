package techcourse.myblog.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.dto.ArticleDto;
import techcourse.myblog.article.service.ArticleService;

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
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Create articleDto) {
        long newArticleId = articleService.save(articleDto);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto.Update articleDto) {
        long updatedArticleId = articleService.update(articleId, articleDto);
        return "redirect:/articles/" + updatedArticleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
