package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.application.ArticleService;
import techcourse.myblog.application.CommentService;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.dto.CommentResponse;
import techcourse.myblog.application.dto.UserResponse;

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
        List<CommentResponse> comments = commentService.findAllByArticle(articleId);
        model.addAttribute(ARTICLE_INFO, articleService.find(articleId));
        model.addAttribute(COMMENTS_INFO, comments);

        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String modifyForm(@PathVariable("articleId") long articleId, Model model, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");
        model.addAttribute(ARTICLE_INFO, articleService.find(articleId, userResponse));

        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String modify(@PathVariable("articleId") long articleId, @ModelAttribute ArticleDto articleDto,
                         HttpSession httpSession, Model model) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");

        ArticleDto updatedArticle = articleService.modify(articleDto, articleId, userResponse);
        model.addAttribute(ARTICLE_INFO, updatedArticle);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String remove(@PathVariable("articleId") long articleId, HttpSession httpSession) {
        UserResponse userResponse = (UserResponse) httpSession.getAttribute("user");

        articleService.remove(articleId, userResponse);

        return "redirect:/";
    }
}
