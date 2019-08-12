package techcourse.myblog.web.controller;

import groovy.util.logging.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.Contents;
import techcourse.myblog.domain.user.LoginUser;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/new")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping("/new")
    public String createArticle(Contents contents, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute("user");
        Article article = articleService.createArticle(contents, author);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        Article article = articleService.findArticle(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findAllComments(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String showUpdateArticle(@PathVariable Long articleId, Model model,@LoginUser User user) {
        Article article = articleService.findArticle(articleId);
        articleService.checkAvailableUpdateUser(article, user);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, Contents contents, @LoginUser User user) {
        articleService.updateArticle(articleId, contents, user);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, @LoginUser User user) {
        articleService.deleteArticle(articleId, user);
        return "redirect:/";
    }
}
