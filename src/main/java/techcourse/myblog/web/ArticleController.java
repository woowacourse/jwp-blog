package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
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
        Article article = articleRepository.findById(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @PostMapping("/write")
    public RedirectView addArticle(Article articleParam) {
        Long latestId = articleRepository.add(articleParam);
        return new RedirectView("/articles/" + latestId);
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepository.findById(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @GetMapping("/articles/new")
    public String getNewArticle() {
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable long articleId, Article articleParam) {
        long updateId = articleRepository.updateById(articleParam, articleId);
        return new RedirectView("/articles/" + updateId);
    }

    @DeleteMapping("articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId) {
        articleRepository.deleteById(articleId);
        return new RedirectView("/");
    }
}
