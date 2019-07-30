package techcourse.myblog.web;

import org.slf4j.Logger;
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
    private static final String REDIRECT = "redirect:";
    private static final String ROOT = "/";
    private static final String ROUTE_ARTICLES = "/articles";
    private static final String ROUTE_WRITING = "/writing";
    private static final String ROUTE_ID = "/{id}";
    private static final String ROUTE_EDIT = "/edit";
    private static final String ROUTE_ARTICLE_ID = ROUTE_ARTICLES + ROUTE_ID;
    private static final String PAGE_ARTICLE = "article";
    private static final String PAGE_EDIT = "article-edit";

    private final ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping(ROUTE_WRITING)
    public String getNewArticlePage() {
        return PAGE_EDIT;
    }

    @PostMapping(ROUTE_ARTICLES)
    public String createArticle(@ModelAttribute(PAGE_ARTICLE) final ArticleDto articleDto) {
        final Article article = new Article(articleDto);
        LOG.trace("{}", article);
        articleRepository.save(article);
        return REDIRECT + ROUTE_ARTICLES + ROOT + article.getId();
    }

    @GetMapping(ROUTE_ARTICLE_ID)
    public ModelAndView showArticle(@PathVariable final Long id) {
        final ModelAndView mav = new ModelAndView(PAGE_ARTICLE);
        mav.addObject(PAGE_ARTICLE, articleRepository.findById(id).get());
        return mav;
    }

    @GetMapping(ROUTE_ARTICLE_ID + ROUTE_EDIT)
    public String editArticlePage(final Model model, @PathVariable final Long id) {
        model.addAttribute(PAGE_ARTICLE, articleRepository.findById(id).get());
        return PAGE_EDIT;
    }

    @PutMapping(ROUTE_ARTICLE_ID)
    public RedirectView editArticle(@PathVariable final Long id, @ModelAttribute(PAGE_ARTICLE) final ArticleDto articleDto) {
        final Article article = articleRepository.findById(id).get();
        article.write(articleDto);
        articleRepository.save(article);
        return new RedirectView(ROUTE_ARTICLES + ROOT + article.getId());
    }

    @DeleteMapping(ROUTE_ARTICLE_ID)
    public String deleteArticle(@PathVariable final Long id) {
        articleRepository.deleteById(id);
        return REDIRECT + ROOT;
    }
}
