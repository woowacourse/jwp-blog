package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String getAllArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public ModelAndView writeArticle(final Article article) {
        Long latestId = articleRepository.generateNewId();
        article.setArticleId(latestId);
        articleRepository.addArticle(article);

        return new ModelAndView("redirect:/articles/" + latestId);
    }

    @PutMapping("/update")
    public String updateArticle(final Article article) {
        articleRepository.updateArticle(article);

        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/articles/{articleId}")
    public String findArticleById(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId).ifPresent(a -> model.addAttribute("article", a));

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable Long articleId, Model model) {
        articleRepository.findArticleById(articleId).ifPresent(a -> model.addAttribute("article", a));

        return "article-edit";
    }

    @DeleteMapping("/articles/{articleId}/delete")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteArticle(articleId);
        return "redirect:/";
    }

}
