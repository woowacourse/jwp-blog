package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

import static techcourse.myblog.web.ControllerUtil.putLoginUser;

@Controller
public class IndexController {
    private final ArticleRepository articleRepository;

    public IndexController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String indexView(HttpSession session, Model model) {
        putLoginUser(session, model);
        List<Article> articles = articleRepository.findAll();

        model.addAttribute("articles", articles);
        return "index";
    }
}
