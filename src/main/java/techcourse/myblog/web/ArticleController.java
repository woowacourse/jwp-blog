package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();

        return "index.html";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit.html";
    }

    @GetMapping("/articles/new")
    public String getNewArticle() {
        return "article-edit.html";
    }
}
