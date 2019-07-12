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
    public String home(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable("articleId") int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        model.addAttribute("articleId", articleId);
        return "article";
    }

    @GetMapping("/articles/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String saveArticle(@ModelAttribute Article article) {
        articleRepository.add(article);
        return "redirect:/articles/" + articleRepository.lastIndex();
    }

    @GetMapping("/articles/{articleId}/edit")
    public String articleUpdateForm(@PathVariable("articleId") int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        model.addAttribute("articleId", articleId);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable("articleId") int articleId, Article article) {
        articleRepository.update(articleId, article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable("articleId") int articleId) {
        articleRepository.remove(articleId);
        return "redirect:/";
    }
}