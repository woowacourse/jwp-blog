package techcourse.myblog.articles;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @PostMapping
    public String write(Article article) {
        Article savedArticle =  articleService.save(article);
        return "redirect:/articles/"+savedArticle.getId();
    }
}
