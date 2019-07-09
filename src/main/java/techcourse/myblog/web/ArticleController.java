package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String editArticle() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public void publishArticle(String title, String backgroundURL, String content, HttpServletResponse response) throws IOException {
        articleRepository.addArticle(Article.of(title, backgroundURL, content));
        response.sendRedirect("/");
    }
}
