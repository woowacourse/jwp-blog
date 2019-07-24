package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleSaveParams;
import techcourse.myblog.service.ArticleService;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(ArticleSaveParams articleSaveParams) {
        Article article = articleService.save(articleSaveParams.toEntity());
        Long id = article.getId();
        return "redirect:/articles/" + id;
    }

    @GetMapping("/{id}")
    public String fetchArticle(@PathVariable long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editArticle(@PathVariable long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String saveEditedArticle(@PathVariable long id, ArticleSaveParams articleSaveParams) {
        articleService.update(articleSaveParams, id);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleService.deleteById(id);
        return "redirect:/";
    }
}
