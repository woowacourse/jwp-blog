package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleRequest;
import techcourse.myblog.dto.ArticleResponse;
import techcourse.myblog.dto.UserResponse;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.utils.model.ModelUtil;
import techcourse.myblog.utils.session.SessionUtil;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.utils.session.SessionContext.USER;

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

        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.getArticle(articleId));
        ModelUtil.addAttribute(model, "comments", articleService.getComments(articleId));

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.getArticle(articleId));

        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(@Valid ArticleRequest articleRequest) {
        UserResponse userResponse = (UserResponse) session.getAttribute(USER);
        ArticleResponse articleResponseDto = articleService.save(articleRequest, userResponse);
        Long articleId = articleResponseDto.getId();

        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/articles/{articleId}")
    public String modifyArticle(@PathVariable long articleId, @Valid ArticleRequest articleRequest) {
        UserResponse userResponse = (UserResponse) SessionUtil.getAttribute(session, USER);
        articleService.update(articleId, articleRequest, userResponse);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);

        return "redirect:/";
    }
}