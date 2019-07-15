package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.error.BadRequestError;
import techcourse.myblog.error.NotExistEntityError;
import techcourse.myblog.error.RequestError;
import techcourse.myblog.web.exception.BadRequestException;
import techcourse.myblog.web.exception.NotExistEntityException;

@Controller
public class ArticleController {
    private static final int MIN_ARTICLE_ID = 1;
    private static final String ERROR_PAGE = "/error/error-page";

    private final ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping("/articles")
    public String createArticles(Article article, Model model) {
        Long id = articleRepository.saveArticle(article);

        model.addAttribute("article", article);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticlePageByArticleId(@PathVariable Long articleId, Model model) {
        try {
            checkArticleId(articleId);
        } catch (BadRequestException e) {
            return sendErrorPage(ERROR_PAGE, model, new BadRequestError(articleId));
        }

        try {
            Article article = articleRepository.findById(articleId);
            model.addAttribute("article", article);
            return "article";
        } catch (NotExistEntityException e) {
            return sendErrorPage(ERROR_PAGE, model, new NotExistEntityError(articleId));
        }
    }

    @GetMapping("/articles/{articleId}/edit")
    public String readArticleEditPage(@PathVariable Long articleId, Model model) {
        try {
            checkArticleId(articleId);
        } catch (BadRequestException e) {
            return sendErrorPage(ERROR_PAGE, model, new BadRequestError(articleId));
        }
        try {
            Article article = articleRepository.findById(articleId);
            model.addAttribute("article", article);
            model.addAttribute("method", "put");
            return "/article-edit";
        } catch (NotExistEntityException e) {
            return sendErrorPage(ERROR_PAGE, model, new NotExistEntityError(articleId));
        }
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, Article article, Model model) {
        try {
            checkArticleId(articleId);
        } catch (BadRequestException e) {
            return sendErrorPage(ERROR_PAGE, model, new BadRequestError(articleId));
        }
        try {
            Article modifiedArticle = articleRepository.findById(articleId);
            modifiedArticle.changeTitle(article.getTitle());
            modifiedArticle.changeCoverUrl(article.getCoverUrl());
            modifiedArticle.changeContents(article.getContents());

            model.addAttribute("article", modifiedArticle);
            return "/article";
        } catch (NotExistEntityException e) {
            return sendErrorPage(ERROR_PAGE, model, new NotExistEntityError(articleId));
        }
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, Model model) {
        try {
            checkArticleId(articleId);
        } catch (BadRequestException e) {
            return sendErrorPage(ERROR_PAGE, model, new BadRequestError(articleId));
        }
        try {
            articleRepository.removeArticle(articleId);
            return "redirect:/";
        } catch (NotExistEntityException e) {
            return sendErrorPage(ERROR_PAGE, model, new NotExistEntityError(articleId));
        }
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
