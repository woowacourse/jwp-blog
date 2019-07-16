package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.Optional;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/writing")
    public ModelAndView createArticleForm() {
        return new ModelAndView("article-edit");
    }

    @PostMapping("/write")
    public RedirectView createArticle(ArticleDto article) {
        Article savedArticle = articleRepository.save(article.toArticle());
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("articles", articleRepository.findAll());
        return mav;
    }


    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Optional<Article> article = articleRepository.findById(articleId);
        article.ifPresent(value -> model.addAttribute("article", value));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView editArticleForm(@PathVariable long articleId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("article-edit");
        mav.addObject("article", articleRepository.findById(articleId));
        return mav;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView editArticle(@PathVariable long articleId, ArticleDto editedArticle) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        if (articleOpt.isPresent()) {
            Article article = articleOpt.get();
            article.update(editedArticle.toArticle());
            articleRepository.save(article);
        }

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId) {
        //articleRepository.delete(articleRepository.findById(articleId));
        return new RedirectView("/");
    }
}
