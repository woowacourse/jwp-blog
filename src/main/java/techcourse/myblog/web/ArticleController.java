package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles")
    public String show() {
        return "article";
    }

    @PostMapping("/articles")
    public String create(Article article) {
        articleRepository.add(article);
        return "/";
    }

    @GetMapping("/articles/new")
    public String showCreatePage() {
        return "article-edit";
    }


}
