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

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();
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
    public String fetchArticle(@PathVariable long id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticle(@PathVariable long id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String saveEditedArticle(@PathVariable long id, Article article, Model model) {
        article.setId((int) id);
        articleRepository.saveEdited(article);
        model.addAttribute("article", article);
        return "article";
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleRepository.delete(id);
        return "redirect:/";
    }
}
