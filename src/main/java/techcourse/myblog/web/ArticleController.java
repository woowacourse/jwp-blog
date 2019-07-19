package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleDto;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String getAllArticles(Model model) {
        log.debug("A debug message");
        log.info("A info message");
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String writeArticle(final ArticleDto articleDto) {
        Article newArticle = articleDto.toArticle();
        articleRepository.addArticle(newArticle);

        return "redirect:/articles/" + newArticle.getArticleId();
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(final ArticleDto articleDto) {
        articleRepository.updateArticle(articleDto.toArticle());
        return "redirect:/articles/" + articleDto.getArticleId();
    }

    @GetMapping("/articles/{articleId}")
    public String findArticleById(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleRepository.findArticleById2(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleRepository.findArticleById2(articleId));
        return "article-edit";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleRepository.deleteArticle(articleId);
        return "redirect:/";
    }

}
