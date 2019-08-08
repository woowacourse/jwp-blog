package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.article.ArticleRequest;
import techcourse.myblog.dto.article.ArticleResponse;
import techcourse.myblog.dto.user.UserResponse;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.utils.annotation.LoginUser;
import techcourse.myblog.utils.model.ModelUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;
import static techcourse.myblog.web.URL.*;

@Controller
public class ArticleController {

    private final HttpSession session;
    private final ArticleService articleService;

    public ArticleController(HttpSession session, ArticleService articleService) {
        this.session = session;
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        ModelUtil.addAttribute(model, "articles", articleService.getArticles());

        return "/index";
    }

    @GetMapping(WRITING)
    public String getArticleEditForm() {
        return ARTICLE_EDIT;
    }

    @GetMapping(ARTICLES + "/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.getArticle(articleId));
        ModelUtil.addAttribute(model, "comments", articleService.getComments(articleId));

        return ARTICLE;
    }

    @GetMapping(ARTICLES + "/{articleId}" + "/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.getArticle(articleId));

        return ARTICLE_EDIT;
    }

    @PostMapping(ARTICLES)
    public String saveArticle(@Valid ArticleRequest articleRequest) {
        UserResponse userResponse = (UserResponse) session.getAttribute(USER);
        ArticleResponse articleResponseDto = articleService.save(articleRequest, userResponse);
        Long articleId = articleResponseDto.getId();

        return REDIRECT + ARTICLES + "/" + articleId;
    }

    @PutMapping(ARTICLES + "/{articleId}")
    public String modifyArticle(@PathVariable long articleId, @Valid ArticleRequest articleRequest, @LoginUser UserResponse loginUser) {
        articleService.update(articleId, articleRequest, loginUser);

        return REDIRECT + ARTICLES + "/" + articleId;
    }

    @DeleteMapping(ARTICLES + "/{articleId}")
    public String deleteArticle(@PathVariable long articleId, @LoginUser UserResponse loginUser) {
        articleService.delete(articleId, loginUser);

        return REDIRECT;
    }
}