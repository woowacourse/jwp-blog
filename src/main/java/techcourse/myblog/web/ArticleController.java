package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public void createArticle(@RequestBody Article article, HttpServletResponse res) throws IOException {
        int articleId = articleRepository.save(article);
        res.sendRedirect("/article/" + articleId);
    }

    @GetMapping("/articles/new")
    public String getNewArticle() {
        return "article-edit";
    }
}
