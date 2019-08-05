package techcourse.myblog.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.IndexService;

import java.util.List;

@Slf4j
@Controller
public class IndexController {
    private final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    @GetMapping("/")
    public String showArticlesPage(Model model) {
        List<Article> articles = indexService.finaAll();
        model.addAttribute("articles", articles);
        return "index";
    }
}
