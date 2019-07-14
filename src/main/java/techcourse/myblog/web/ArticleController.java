package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Optional;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String writing(Model model) {
        model.addAttribute("method", "POST");
        return "article-edit";
    }

    @PostMapping("/articles")
    public String addArticle(@ModelAttribute Article article) {
        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable long id, Model model) {
        addAttributeArticleModel(id, model);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticle(@PathVariable long id, Model model) {
        addAttributeArticleModel(id, model);
        model.addAttribute("method", "PUT");
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String putArticle(@PathVariable long id, Article article) {
        articleRepository.updateArticleById(article, id);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleRepository.removeArticleById(id);
        return "redirect:/";
    }

    private void addAttributeArticleModel(@PathVariable long id, Model model) {
        Optional<Article> maybeArticle = articleRepository.getArticleById(id);
        maybeArticle.ifPresent(ariticle -> model.addAttribute("article", ariticle));
        maybeArticle.orElseThrow(IllegalArgumentException::new);
    }
}
