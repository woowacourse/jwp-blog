package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/writing")
    public String editArticle() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public void publishArticle(String title, String coverUrl, String contents, HttpServletResponse response) throws IOException {
        articleRepository.addArticle(Article.of(title, coverUrl, contents));
        response.sendRedirect("/");
    }

    @GetMapping("/articles/{articleId}")
    public String retrieveArticleById(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId)
            .orElseThrow(() -> new IllegalArgumentException("Article not found: " + articleId));
        model.addAttribute("article", article);
        return "article";
    }
}
