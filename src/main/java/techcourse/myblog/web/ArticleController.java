package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleRepository articleRepository;

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(Article article, Model model) {
        Article newArticle = articleRepository.save(article);
        model.addAttribute("article", newArticle);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable int articleId, Model model) {
        model.addAttribute("article",
                articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다")));
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable int articleId, Model model) {
        model.addAttribute("article",
                articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다.")));
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, Article article, Model model) {
        article.setId(articleId);
        Article newArticle = articleRepository.save(article);
        model.addAttribute("article", newArticle);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
