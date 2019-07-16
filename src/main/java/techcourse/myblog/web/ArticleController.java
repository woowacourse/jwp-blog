package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        return "article";
    }

    @GetMapping("/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String saveArticle(@ModelAttribute Article article) {
        articleRepository.add(article);
        return "redirect:/articles/" + articleRepository.lastIndex();
    }

    @GetMapping("/{articleId}/edit")
    public String articleUpdateForm(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.get(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable int articleId, Article article) {
        articleRepository.update(articleId, article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.remove(articleId);
        return "redirect:/";
    }
}