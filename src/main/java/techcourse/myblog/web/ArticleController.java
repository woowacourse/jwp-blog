package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {

    private ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public ModelAndView index(String blogName) {
        if (blogName == null) {
            blogName = "누구게?";
        }
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("blogName", blogName);
        modelAndView.addObject("articles", articleRepository.findAll());
        return modelAndView;
    }

    @GetMapping("/writing")
    public String writeForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public ModelAndView save(Article article) {
        articleRepository.save(article);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/article/{articleId}")
    public ModelAndView show(@PathVariable String articleId) {
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView writeForm(@PathVariable String articleId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("article-edit");
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView update(@PathVariable String articleId, Article newArticle) {
        Article article = articleRepository.findArticleById(Integer.parseInt(articleId));
        article.update(newArticle);
        return new RedirectView("/");
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable String articleId) {
        articleRepository.removeById(Integer.parseInt(articleId));
        System.out.println("삭제 된 이후에 사이즈 : " + articleRepository.findAll().size());
        return new RedirectView("/");
    }
}
