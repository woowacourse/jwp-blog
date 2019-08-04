package techcourse.myblog.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.service.ArticleService;

@RequestMapping("/")
@Controller
public class HomeController {
    private final ArticleService articleService;

    public HomeController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String home(Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 2) Pageable pageable) {
        model.addAttribute("articles", articleService.getAllArticles(pageable));
        return "index";
    }

}
