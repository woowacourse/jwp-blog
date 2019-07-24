package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

import techcourse.myblog.repository.ArticleRepository;

@Controller
public class IndexController {
    private final ArticleRepository articleRepository;

    @Autowired
    public IndexController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(HttpSession httpSession, Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        model.addAttribute("user", httpSession.getAttribute("user"));

        return "index";
    }
}
