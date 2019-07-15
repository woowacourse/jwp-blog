package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

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
    public RedirectView createArticle(ArticleDto articleDto) {
        Article article = articleRepository.save(articleDto);
        return new RedirectView("/articles/" + article.getId());
    }

    @GetMapping("/")
    public ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("index");
        mav.addObject("articles", articleRepository.findAll());
        return mav;
    }

    @GetMapping("/articles/{articleId}")
    public ModelAndView showArticle(@PathVariable int articleId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("article");
        mav.addObject("article", articleRepository.findById(articleId));
        return mav;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView editArticleForm(@PathVariable int articleId) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("article-edit");
        mav.addObject("article", articleRepository.findById(articleId));
        return mav;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView editArticle(@PathVariable int articleId, ArticleDto articleDto) {
        Article article = articleDto.toArticle(articleId);
        articleRepository.modify(article);
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable int articleId) {
        articleRepository.remove(articleId);
        return new RedirectView("/");
    }
}
