package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        // TODO: 2019-07-10 아이디 처리...
        article.setId(articleRepository.getLastId() + 1);

        articleRepository.add(article);

        model.addAttribute(article);
        // redirect?
        return "article";
    }

    @GetMapping("/articles/{id}")
    public String detail(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        return "article";
    }

    @GetMapping("/")
    public String index(Model model){
        model.addAttribute("articles",articleRepository.getArticles());
        return "index";
    }
}
