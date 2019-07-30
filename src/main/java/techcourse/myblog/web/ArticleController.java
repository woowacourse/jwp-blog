package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleRequestDto;
import techcourse.myblog.dto.UserResponseDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.utils.model.ModelUtil;

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
        ModelUtil.addAttribute(model, "articles", articleService.findAll());

        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.findArticle(articleId));
        ModelUtil.addAttribute(model, "comments", articleService.getCommentsByArticleId(articleId));

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        ModelUtil.addAttribute(model, "article", articleService.findArticle(articleId));

        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(@Valid ArticleRequestDto articleRequestDto) {
        UserResponseDto userResponseDto = (UserResponseDto) session.getAttribute(USER);
        Article article = articleService.save(articleRequestDto, userResponseDto);

        return "redirect:/articles/" + article.getId();
    }

    @PutMapping("/articles/{articleId}")
    public String modifyArticle(@PathVariable long articleId, @Valid ArticleRequestDto articleRequestDto) {
        articleService.update(articleId, articleRequestDto);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);

        return "redirect:/";
    }
}