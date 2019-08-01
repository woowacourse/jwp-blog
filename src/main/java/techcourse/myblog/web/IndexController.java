package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.ArticleService;

@Controller
public class IndexController {
    private ArticleService articleService;

    public IndexController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAllWithCommentCount());
        return "index";
    }
}
