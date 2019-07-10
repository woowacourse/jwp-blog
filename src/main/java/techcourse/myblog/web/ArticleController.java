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
    public String articleCreateForm(Model model) {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(Article article, Model model) {
        articleRepository.add(article);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}")
    public String viewArticle(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticle(@PathVariable int articleId, Model model){
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article-edit";
    }
}
