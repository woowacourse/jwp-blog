package techcourse.myblog.web;

import groovy.util.logging.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.user.User;

import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private ArticleService articleService;
    private CommentService commentService;

    ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/new")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping("/new")
    public String createArticle(ArticleDto articleDto, Model model, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute("user");
        Article article = articleService.createArticle(articleDto, author);
        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        Article article = articleService.findArticle(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", articleService.findAllComments(article));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String showUpdateArticle(@PathVariable Long articleId, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Article article = articleService.findArticle(articleId);
        articleService.checkAvailableUpdateUser(article, user);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto updateArticleDto, Model model, HttpSession httpSession) {
        User author = (User) httpSession.getAttribute("user");
        Article updateArticle = articleService.updateArticle(articleId, updateArticleDto.toEntity(author));
        model.addAttribute("article", updateArticle);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.deleteArticle(articleId, user);
        return "redirect:/";
    }
}
