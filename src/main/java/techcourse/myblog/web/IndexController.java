package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.ArticleService;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }
}
