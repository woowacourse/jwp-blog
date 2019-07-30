package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.service.ArticleService;

@RequestMapping("/")
@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String home(Model model){
        model.addAttribute("articles", articleService.getAllArticles());
        return "index";
    }

}
