package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        Iterable<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView addArticle(final Article articleParam) {
        Article article = articleRepository.save(articleParam);
        return new RedirectView("/articles/" + article.getId());
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable final long articleId, final Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable final long articleId, final Article articleParam) {
        articleRepository.update(articleParam);
        articleParam.setId(articleId);
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        return new RedirectView("/articles/" + article.getId());
    }

    @DeleteMapping("articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable final long articleId) {
        System.out.println(articleId);
        System.out.println(articleRepository.findAll());
        articleRepository.deleteById(articleId);
        return new RedirectView("/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable final long articleId, final Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article-edit";
    }
}
