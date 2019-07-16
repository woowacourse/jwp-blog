package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.repository.ArticleRepository;

@Controller
public class HomeController {

    @Autowired
    ArticleRepository articleRepository;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }
}
