package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class HomeController {
    private final ArticleRepository articleRepository;

    public HomeController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }
}
