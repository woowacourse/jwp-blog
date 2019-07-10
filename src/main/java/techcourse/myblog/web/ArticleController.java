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
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable long articleId, Model model) {
        System.out.println("aaaaaa" + articleId);
        Article article = articleRepository.findById(articleId);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("coverUrl", article.getCoverUrl());
        model.addAttribute("contents", article.getContents());
        return "article";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit.html";
    }

    @PostMapping("/write")
    public String addArticle(Article articleParam, Model model) {
        Long latestId = articleRepository.add(articleParam);
        Article article = articleRepository.findById(latestId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/new")
    public String getNewArticle() {
        return "article-edit";
    }
}
