package techcourse.myblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String showMain(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }
}
