package techcourse.myblog.presentation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.error.RequestError;
import techcourse.myblog.web.exception.BadRequestException;

@Controller
public class ArticleController {
    private static final int MIN_ARTICLE_ID = 1;
    private static final String ERROR_PAGE = "/error/error-page";

    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping("/articles")
    public RedirectView createArticles(ArticleDto article) {
        String title = article.getTitle();
        String coverUrl = article.getCoverUrl();
        String contents = article.getContents();

        Long id = articleRepository.save(new Article(title, coverUrl, contents)).getId();

        return new RedirectView("/articles/" + id);
    }

    @GetMapping("/articles/{articleId}")
    public ModelAndView readArticlePageByArticleId(@PathVariable Long articleId) {
        Article article = articleRepository.findById(articleId).get();

        ModelAndView modelAndView = new ModelAndView("/article");
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @GetMapping("/articles/{articleId}/edit")
    public ModelAndView readArticleEditPage(@PathVariable Long articleId) {
        ModelAndView modelAndView = new ModelAndView("/article-edit");
        Article article = articleRepository.findById(articleId).get();
        ArticleDto articleDto = article.toDto();

        modelAndView.addObject("article", article);
        modelAndView.addObject("method", "put");
        return modelAndView;
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView updateArticle(@PathVariable Long articleId, ArticleDto article) {
        RedirectView redirectView = new RedirectView("/articles/" + articleId);

        Article modifiedArticle = articleRepository.findById(articleId).get();
        modifiedArticle.update(new Article.ArticleBuilder().title(article.getTitle())
                .coverUrl(article.getCoverUrl())
                .contents(article.getContents())
                .build());
        articleRepository.save(modifiedArticle);

        return redirectView;
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId) {
        RedirectView redirectView = new RedirectView("/");
        articleRepository.deleteById(articleId);
        return redirectView;
    }


    private void checkArticleId(Long articleId) {
        if (isLowerThanMinArticleId(articleId)) {
            throw new BadRequestException();
        }
    }

    private boolean isLowerThanMinArticleId(Long articleId) {
        return articleId < MIN_ARTICLE_ID;
    }

    private String sendErrorPage(String pageName, Model model, RequestError requestError) {
        model.addAttribute("error", requestError);
        return pageName;
    }
}
