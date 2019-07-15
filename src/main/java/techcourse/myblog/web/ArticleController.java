package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.dto.ArticleDto;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, Model model) {
        Article article = new Article(articleRepository.newArticleId(), articleDto);
        model.addAttribute("article", article);
        articleRepository.add(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{articleId}")
    public String selectArticle(@PathVariable int articleId, Model model) {
        model.addAttribute("article", this.articleRepository.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticleForm(@PathVariable int articleId, Model model) {
        model.addAttribute("article", this.articleRepository.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, ArticleDto articleDto, Model model) {
        articleRepository.update(articleId, articleDto);
        Article updatedArticle = articleRepository.findById(articleId);
        model.addAttribute("article", updatedArticle);
        return "redirect:/articles/" + updatedArticle.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.remove(articleId);
        return "redirect:/";
    }
}
