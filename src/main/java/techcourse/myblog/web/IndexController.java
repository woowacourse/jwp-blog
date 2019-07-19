package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.ArticleService;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }
}
