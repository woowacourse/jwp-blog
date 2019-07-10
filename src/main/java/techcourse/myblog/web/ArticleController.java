package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping(value = {"/", "/articles"})
    public String show(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @PostMapping("/articles")
    public String create(Article article, Model model) {
        articleRepository.add(article);
        return "redirect:/articles";
    }

    @GetMapping("/articles/new")
    public String showCreatePage() {
        return "article-edit";
    }
}
