package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
        model.addAttribute("article", new Article());
        model.addAttribute("url", "/articles");
        model.addAttribute("method", "post");
        return "article-edit";
    }

    @ResponseBody
    @PostMapping("/articles")
    public ModelAndView confirmWrite(final Article article) {
        articleRepository.write(article);
        return new ModelAndView("redirect:/");
    }

    @GetMapping("/articles/{articleId}/")
    public String viewArticle(@PathVariable final int articleId, final Model model) {
        model.addAttribute("article", articleRepository.find(articleId).orElseThrow(IllegalArgumentException::new));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit/")
    public String editArticle(@PathVariable final int articleId, final Model model) {
        model.addAttribute("article", articleRepository.find(articleId).orElseThrow(IllegalArgumentException::new));
        model.addAttribute("url", "../");
        model.addAttribute("method", "put");
        return "article-edit";
    }

    @ResponseBody
    @PutMapping("/articles/{articleId}/")
    public ModelAndView confirmEdit(@PathVariable final int articleId, final Article article) {
        articleRepository.edit(article, articleId);
        return new ModelAndView("redirect:/");
    }

    @ResponseBody
    @DeleteMapping("/articles/{articleId}/")
    public ModelAndView confirmEdit(@PathVariable final int articleId) {
        articleRepository.delete(articleId);
        return new ModelAndView("redirect:/");
    }
}