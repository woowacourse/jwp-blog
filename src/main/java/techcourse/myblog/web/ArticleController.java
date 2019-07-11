package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.Articles;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        Articles articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/new")
    public String writeNewArticle() {

        return "article-edit";
    }

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(Article article, Model model) {
        articleRepository.save(article);
        model.addAttribute("article", article);
        System.out.println(articleRepository.findAll());
        return "article";
    }

    @GetMapping("/articles/{id}")
    public String fetchArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticle(@PathVariable int id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String saveEditedArticle(@PathVariable int id, Article article, Model model) {
        article.setId(id);
        articleRepository.saveEdited(article);
        model.addAttribute("article", article);
        return "article";
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.delete(id);
        return "redirect:/";
    }
}
