package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.repository.ArticleRepository;

import javax.servlet.http.HttpSession;

@Controller
public class IndexController {
    private final ArticleRepository articleRepository;

    @Autowired
    public IndexController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession httpSession) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("user", httpSession.getAttribute("user"));

        return "index";
    }
}