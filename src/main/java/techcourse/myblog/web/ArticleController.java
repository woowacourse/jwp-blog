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

//    @GetMapping("/articles")
//    public String index(Model model) {
//        model.addAttribute("articles", articleRepository.findAll());
//        return "index";
//    }

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
    public String createArticle(Article article) {
        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

//    @GetMapping("/article/{articleId}")
//    public ModelAndView show(@PathVariable String articleId) {
//        Article article = articleRepository.findById(Long.parseLong(articleId))
//                .orElseThrow(IllegalArgumentException::new);
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("article");
//        modelAndView.addObject("article", article);
//        return modelAndView;
//    }

    @GetMapping("/articles/{id}")
    public ModelAndView show(@PathVariable long id) {
        ModelAndView mav = new ModelAndView("article");
        mav.addObject("article", articleRepository.findById(id).get());
        return mav;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView writeForm(@PathVariable String articleId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("article-edit");
        Article article = articleRepository.findById(Long.parseLong(articleId))
                .orElseThrow(IllegalArgumentException::new);
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView update(@PathVariable String articleId, Article newArticle) {
        Article article = articleRepository.findById(Long.parseLong(articleId))
                .orElseThrow(IllegalArgumentException::new);
        article.update(newArticle);
        return new RedirectView("/");
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable String articleId) {
        articleRepository.deleteById(Long.parseLong(articleId));
        return new RedirectView("/");
    }
}
