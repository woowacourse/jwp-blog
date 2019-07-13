package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.ArticleVO;

@Controller
public class ArticleController {

    private final ArticleRepository articleRepository;

    ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleVO articleVO, Model model) {
        Article article = new Article(articleVO.getTitle(), articleVO.getCoverUrl(), articleVO.getContents());
        articleRepository.save(article);
        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String showUpdatedArticle(@PathVariable Long articleId, ArticleVO updatedArticle, Model model) {
        articleRepository.update(articleId, updatedArticle);
        model.addAttribute("article", updatedArticle);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("delete/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.delete(articleId);
        return "redirect:/";
    }
}
