package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }


    @PostMapping("/articles")
    public String writeArticle(HttpServletRequest request) {
        articleRepository.save(new Article(request.getParameter("title"), request.getParameter("contents"), request.getParameter("coverUrl")));
        return "article";
    }
}
