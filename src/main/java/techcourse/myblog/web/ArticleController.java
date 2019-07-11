package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @PostMapping("/articles")
    public String createArticles(Article article, Model model) {
        articleRepository.insertArticle(article);

        model.addAttribute("article", article);

        return "article";
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable int articleId, Model model) {
        Article article = articleRepository.findById(articleId);

        model.addAttribute("article", article);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String readArticleEditPage(@PathVariable int articleId, Model model) {
        Article article = articleRepository.findById(articleId);

        model.addAttribute("article", article);

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, Article article, Model model) {
        articleRepository.updateArticle(articleId, article);

        model.addAttribute("article", article);

        return "article";
    }
}
