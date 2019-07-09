package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/articles/{articleId}")
    public String fetchArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepository.find(articleId);
        model.addAttribute("article", article);
        return "article";
    }
}
