package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleNotFoundException;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping(value = {"/", "/articles"})
    public String showArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @PostMapping("/articles")
    public String createArticle(Article article) {
        articleRepository.add(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/new")
    public String showCreatePage() {
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") long id, Model model) {
        Article foundArticle = articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
        model.addAttribute("article", foundArticle);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") long id, Model model) {
        Article foundArticle = articleRepository.findById(id)
                .orElseThrow(ArticleNotFoundException::new);
        model.addAttribute("article", foundArticle);
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") long id, Article newArticle) {
        articleRepository.findById(id)
                .ifPresent(article -> article.updateArticle(newArticle));
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id) {
        articleRepository.deleteById(id);
        return "redirect:/";
    }
}
