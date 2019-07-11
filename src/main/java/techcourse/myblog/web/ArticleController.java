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
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    private String getIndex(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{id}")
    private String getArticleById(@PathVariable int id, Model model) {
        Article article = articleRepository.findByIndex(id);
        model.addAttribute("id", id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/writing")
    private String getWritingArticle() {
        return "article-edit";
    }

    @GetMapping("/articles/{id}/edit")
    private String getEditArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.findByIndex(id);
        model.addAttribute("id", id);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PostMapping("/articles")
    private String postArticle(Article article) {
        articleRepository.addBlog(article);
        int id = articleRepository.findAll().size() - 1;

        return "redirect:/articles/" + id;
    }

    @PutMapping("/articles/{id}")
    private String putArticle(@PathVariable int id, Article article) {
        Article originArticle = articleRepository.findByIndex(id);
        originArticle.updateByArticle(article);

        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    private String deleteArticleById(@PathVariable int id) {
        articleRepository.deleteByIndex(id);
        return "redirect:/";
    }
}
