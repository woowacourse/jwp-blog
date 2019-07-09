package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;


    @GetMapping("/articles/1")
    private String getArticleById(Model model) {
        Article article = articleRepository.find(1);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("contents", article.getContents());
        return "article";
    }

    @GetMapping("/articles/new")
    private String getArticle(Model model) {
        model.addAttribute("message", "articles");
        return "article-edit";
    }
}
