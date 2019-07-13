package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String showMain(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(Article article) {
        article.setId();
        articleRepository.addArticle(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.findArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.findArticleById(id));
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable int id, Article article) {
        articleRepository.editArticle(id, article);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.deleteArticle(id);
        return "redirect:/";
    }
}
