package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String writing(){
        return "article-edit";
    }

    @PostMapping("/articles")
    public String posting(Article article, Model model) {

        article.setId(articleRepository.getLastId());

        articleRepository.add(article);

        model.addAttribute(article);
        // redirect?
        return "article";
    }
}
