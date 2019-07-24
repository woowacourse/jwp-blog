package techcourse.myblog.web;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import static org.slf4j.LoggerFactory.getLogger;

@Controller
public class ArticleController {
    private static final Logger LOG = getLogger(ArticleController.class);

    @Autowired
    private ArticleRepository articleRepository;

//    @Autowired
//    void setArticleRepository(final ArticleRepository articleRepository) {
//        this.articleRepository = articleRepository;
//    } // TODO: 검색 - 생성자 주입 필드 주입

    @GetMapping("/writing")
    public String getNewArticlePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(@ModelAttribute("article") final ArticleDto articleDto) {
        final Article article = new Article(articleDto);
        LOG.trace("{}", article);
        articleRepository.save(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public ModelAndView showArticle(@PathVariable final Long id) {
        final ModelAndView mav = new ModelAndView("article");
        mav.addObject("article", articleRepository.findById(id).get());
        return mav;
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticlePage(final Model model, @PathVariable final Long articleId) {
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView editArticle(@PathVariable final Long articleId, @ModelAttribute("article") final ArticleDto articleDto) {
        final Article article = articleRepository.findById(articleId).get();
        article.write(articleDto);
        articleRepository.save(article);
        return new RedirectView("/articles/" + article.getId());
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable final Long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
