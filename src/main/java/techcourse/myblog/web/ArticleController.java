package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String articlesNew() {
        return "article-edit";
    }

    @GetMapping("/article-edit")
    public String write() {
        return "article-edit";
    }


    @PostMapping("/write")
    public String createPosting(String title, String coverUrl, String contents, Model model) {
        Article article = new Article(title, coverUrl, contents);

        articleRepository.save(article);
        model.addAttribute("article", article);
        return "article";
    }
}
