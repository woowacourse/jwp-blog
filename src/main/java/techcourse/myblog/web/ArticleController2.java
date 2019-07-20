package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article2;
import techcourse.myblog.domain.ArticleRepository2;

import java.util.Optional;

@Controller
public class ArticleController2 {

    private final ArticleRepository2 articleRepository;

    private static final Logger log =
            LoggerFactory.getLogger(ArticleController2.class);


    public ArticleController2(ArticleRepository2 articleRepository) {
        this.articleRepository = articleRepository;
    }

//    @GetMapping("/")
//    public String index(Model model) {
//        model.addAttribute("articles", articleRepository.findAll());
//        return "index";
//    }

    @GetMapping("/")
    public String index(Model model) {
        log.trace("A TRACE Message");
        log.debug("A DEBUG Message");
        log.info("An INFO Message");
        log.warn("A WARN Message");
        log.error("An ERROR Message");
        

        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(@ModelAttribute Article2 article, Model model) {
        model.addAttribute("article", article);
        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }
//
//    @GetMapping("/articles/{articleId}")
//    public ModelAndView show(@PathVariable long id) {
//        ModelAndView mav = new ModelAndView("article");
//        mav.addObject("article", articleRepository.findById(id).get());
//        return mav;
//    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        Optional<Article2> article = articleRepository.findById(articleId);
        model.addAttribute("article", article.get());
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticle(@PathVariable Long articleId, Model model) {
        Optional<Article2> article = articleRepository.findById(articleId);
        model.addAttribute("article", article.get());
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String showUpdatedArticle(@PathVariable Long articleId, @ModelAttribute Article2 article) {
        article.setId(articleId);
//        articleRepository.modify(article);
        return "redirect:/articles/" + article.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
//        articleRepository.removeById(articleId);
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
