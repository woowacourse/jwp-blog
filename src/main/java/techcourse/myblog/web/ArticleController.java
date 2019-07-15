package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.ArticleRequestDto;
import techcourse.myblog.domain.ArticleResponseDto;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping("/articles")
    public String createArticles(ArticleRequestDto articleRequestDto, Model model) {
        Article article = articleRequestDto.toArticle();
        int id = articleRepository.insertArticle(article);

        model.addAttribute("article", ArticleResponseDto.from(article));

        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable int articleId, Model model) {
        Article article = articleRepository.findById(articleId);

        model.addAttribute("article", ArticleResponseDto.from(article));

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String readArticleEditPage(@PathVariable int articleId, Model model) {
        Article article = articleRepository.findById(articleId);

        model.addAttribute("article", ArticleResponseDto.from(article));
        model.addAttribute("method", "put");

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, ArticleRequestDto articleRequestDto, Model model) {
        Article article = articleRequestDto.toArticle();
        article = article.replaceId(articleId);

        articleRepository.updateArticle(article);

        model.addAttribute("article", ArticleResponseDto.from(article));

        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.deleteArticle(articleId);

        return "redirect:/";
    }
}
