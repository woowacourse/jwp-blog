package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.articles.ArticleService;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String main(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }
}
