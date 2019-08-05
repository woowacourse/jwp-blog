package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.ArticleDetails;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.resolver.Session;
import techcourse.myblog.resolver.UserSession;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage(Model model) {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDetails articleDetails, @Session UserSession userSession) {
        Long newArticleId = articleService.save(userSession.getUserDto(), articleDetails);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        List<CommentDto.Response> comments = commentService.findAllByArticle(articleId);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable Long articleId, @Session UserSession userSession, Model model) {
        model.addAttribute("article", articleService.findById(userSession.getUserDto(), articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDetails articleDetails, @Session UserSession userSession) {
        ArticleDto updatedArticle = articleService.update(userSession.getUserDto(), articleId, articleDetails);
        return "redirect:/articles/" + updatedArticle.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, @Session UserSession userSession) {
        articleService.deleteById(userSession.getUserDto(), articleId);
        return "redirect:/";
    }
}
