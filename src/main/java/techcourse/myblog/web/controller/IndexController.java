package techcourse.myblog.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.ArticleReadService;

@Controller
public class IndexController {
    private final ArticleReadService articleReadService;

    @Autowired
    public IndexController(ArticleReadService articleReadService) {
        this.articleReadService = articleReadService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleReadService.findAll());

        return "index";
    }
}