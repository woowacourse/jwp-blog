package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(final Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticle(final Model model) {
        model.addAttribute("url", "/articles");
        return "article-edit";
    }

    @ResponseBody
    @PostMapping("/articles")
    public RedirectView confirmWrite(final Article article) {
        articleRepository.write(article);
        return new RedirectView("/articles/" + article.getNumber() + "/");
    }

    @GetMapping("/articles/{articleId}/")
    public String viewArticle(@PathVariable final int articleId, final Model model) {
        model.addAttribute("article", articleRepository.find(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit/")
    public String editArticle(@PathVariable final int articleId, final Model model) {
        model.addAttribute("article", articleRepository.find(articleId));
        model.addAttribute("url", "../");
        return "article-edit";
    }

    @ResponseBody
    @PutMapping("/articles/{articleId}/")
    public RedirectView confirmEdit(@PathVariable final int articleId, final Article article) {
        articleRepository.edit(article, articleId);
        return new RedirectView("/articles/" + articleId + "/");
    }

    @ResponseBody
    @DeleteMapping("/articles/{articleId}/")
    public RedirectView deleteArticle(@PathVariable final int articleId) {
        articleRepository.delete(articleId);
        return new RedirectView("/");
    }
}