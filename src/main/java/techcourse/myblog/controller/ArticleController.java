package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.model.Article;
import techcourse.myblog.repository.ArticleRepository;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        model.addAttribute("articleId", articleId);
        return "article";
    }

    @GetMapping("/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String saveArticle(Article article) {
        articleRepository.add(article);
        return "redirect:/articles/" + articleRepository.lastIndex();
    }

    @GetMapping("/{articleId}/edit")
    public String articleUpdateForm(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        model.addAttribute("articleId", articleId);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable int articleId, Article article) {
        articleRepository.update(articleId, article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.remove(articleId);
        return "redirect:/";
    }
}