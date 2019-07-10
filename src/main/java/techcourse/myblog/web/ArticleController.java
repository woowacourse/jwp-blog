package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(HttpServletRequest request, Model model) {
        int articleId = articleRepository.getSize();
        articleRepository.save(new Article(request.getParameter("title"), request.getParameter("contents"), request.getParameter("coverUrl")));
        model.addAttribute("article", findArticleById(articleId));
        return "article";
    }

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable int articleId, Model model) {
        model.addAttribute("article", findArticleById(articleId));
        return "article";
    }

    private Article findArticleById(int articleId) {
        Article article = articleRepository.findById(articleId);
        if (article == null) {
            throw new ArticleNotFoundException();
        }
        return article;
    }
}
