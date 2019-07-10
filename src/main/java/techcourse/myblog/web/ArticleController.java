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
    public String home(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/{articleId}")
    public String article(@PathVariable("articleId") int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        return "article";
    }

    @GetMapping("/articles/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String createArticle(@ModelAttribute Article article, Model model){
        articleRepository.add(article);
        model.addAttribute("article", article);
        return "article";
    }
}
