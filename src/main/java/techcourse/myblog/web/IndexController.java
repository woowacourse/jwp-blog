package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import techcourse.myblog.service.ArticleService;

@Controller
@RequestMapping("/")
public class IndexController {
    private ArticleService articleService;

    public IndexController(ArticleService articleService) {
	this.articleService = articleService;
    }

    @GetMapping
    public String index(Model model) {
	model.addAttribute("articles", articleService.findAll());
	return "index";
    }
}
