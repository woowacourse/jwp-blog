package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("articles",articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @GetMapping("/writing")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(@ModelAttribute Article article, Model model) {
        model.addAttribute("article",article);
        articleRepository.save(article);
        return "article";
    }
}
