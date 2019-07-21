package techcourse.myblog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class MyblogController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String getIndex(Model model) {
        List<Article> articles = articleRepository.findAll();

        model.addAttribute("articles", articles);

        return "index";
    }

    @GetMapping("/writing")
    public String getWriting() {
        return "article-edit";
    }
}
