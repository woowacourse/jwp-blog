package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
@RequestMapping(ArticleController.ARTICLE_URL)
public class ArticleController {
    public static final String ARTICLE_URL = "/articles";
    private final ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/new")
    public String articleForm() {
        return "article-edit";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model) {
        //TODO 에러처리
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable int id, Model model) {
        Article article = articleRepository.findById(id).get();
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PostMapping
    public ModelAndView write(Article article) {
        articleRepository.save(article);
        RedirectView redirectView = new RedirectView(ARTICLE_URL + "/" + article.getId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(redirectView);

        return modelAndView;
    }

    @PutMapping("/{id}")
    public ModelAndView edit(Article article) {
        articleRepository.update(article);

        RedirectView redirectView = new RedirectView(ARTICLE_URL + "/" + article.getId());
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(redirectView);

        return modelAndView;
    }

    @DeleteMapping("/{id}")
    public ModelAndView delete(@PathVariable int id) {
        articleRepository.deleteById(id);

        RedirectView redirectView = new RedirectView("/");
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(redirectView);

        return modelAndView;

    }
}
