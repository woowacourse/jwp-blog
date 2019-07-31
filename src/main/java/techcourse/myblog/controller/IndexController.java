package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;
import techcourse.myblog.service.ArticleService;

@Controller
@AllArgsConstructor
public class IndexController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }
}
