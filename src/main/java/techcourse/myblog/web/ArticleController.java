package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/new")
    public String showCreatePage() {
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") long id, Model model) {
        model.addAttribute("article", articleRepository.findById(id));
        return "article";
    }
}
