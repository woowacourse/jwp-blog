package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

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
        try {
            model.addAttribute("article",
                    articleRepository.findById(articleId).orElseThrow(() -> new IllegalArgumentException("잘못된 접근입니다.")));
        } catch (IllegalArgumentException e) {
            model.addAttribute("message", e);
            return "redirect:/err";
        }
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, Article article, Model model) {
        Article dbArticle = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        dbArticle.update(article);
        articleRepository.save(dbArticle);
        model.addAttribute("article", dbArticle);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
