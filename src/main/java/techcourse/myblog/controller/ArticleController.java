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
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable("articleId") int articleId, Model model) {
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
    public String articleUpdateForm(@PathVariable("articleId") int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        model.addAttribute("articleId", articleId);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable("articleId") int articleId, Article article) {
        articleRepository.update(articleId, article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable("articleId") int articleId) {
        articleRepository.remove(articleId);
        return "redirect:/";
    }
}