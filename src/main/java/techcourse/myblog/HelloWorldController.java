package techcourse.myblog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class HelloWorldController {
    private final ArticleRepository articleRepository;

    public HelloWorldController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String passParamWithGet(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

//    @PostMapping
//    public String passParamWithPost(@RequestBody String blogName){
//        return blogName;
//    }
}
