package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.ArticleService;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.UserResponse;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ArticleController {
    private static final String ARTICLE_INFO = "article";
    private static final String ARTICLES_INFO = "articles";
    private static final String COMMENTS_INFO = "comments";

    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String indexForm(Model model) {
        model.addAttribute(ARTICLES_INFO, articleService.findAll());

        return "index";
    }

    @GetMapping("/writing")
    public String saveForm(Model model) {
        model.addAttribute(ARTICLE_INFO, null);

        return "article-edit";
    }

    @PostMapping("/articles")
    public String save(@Valid ArticleDto articleDto, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        Long articleId = articleService.save(articleDto, userResponse);

        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String show(@PathVariable("articleId") long articleId, Model model) {
        Article article = articleService.findById(articleId);
        List<Comment> comments = commentService.findAllByArticle(article);
        model.addAttribute(ARTICLE_INFO, article);
        model.addAttribute(COMMENTS_INFO, comments);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String modifyForm(@PathVariable("articleId") long articleId, Model model, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        Article article = articleService.findByUser(articleId, userResponse);
        model.addAttribute(ARTICLE_INFO, article);

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String modify(@PathVariable("articleId") long articleId, @ModelAttribute ArticleDto articleDto,
                         HttpSession httpSession, Model model) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");

        ArticleDto updatedArticle = articleService.modify(articleDto, articleId, userResponse);
        model.addAttribute(ARTICLE_INFO, updatedArticle);

        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String remove(@PathVariable("articleId") long articleId, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");

        articleService.remove(articleId, userResponse);

        return "redirect:/";
    }
}
