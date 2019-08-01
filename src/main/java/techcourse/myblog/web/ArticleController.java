package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.UserService;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.dto.UserPublicInfoDto;
import techcourse.myblog.service.exception.UserAuthorizationException;
import techcourse.myblog.web.util.LoginChecker;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class ArticleController {
    private ArticleService articleService;
    private UserService userService;
    private CommentService commentService;
    private LoginChecker loginChecker;

    public ArticleController(ArticleService articleService, UserService userService, CommentService commentService
            , LoginChecker loginChecker) {
        this.articleService = articleService;
        this.userService = userService;
        this.commentService = commentService;
        this.loginChecker = loginChecker;
    }

    @GetMapping("/articles")
    public String showArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/articles/new")
    public String showCreatePage(HttpSession session) {
        loginChecker.getLoggedInUser(session);
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") Long id, Model model) {
        ArticleDto articleDto = articleService.findArticleDtoById(id);
        model.addAttribute("article", articleDto);

        UserPublicInfoDto userPublicInfoDto = userService.findUserPublicInfoByArticle(articleDto);
        model.addAttribute("articleUser", userPublicInfoDto);

        List<CommentResponseDto> commentResponses = commentService.findCommentsByArticleId(id);
        model.addAttribute("comments", commentResponses);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") Long articleId, Model model, HttpSession session) {
        try {
            UserPublicInfoDto userPublicInfoDto = loginChecker.getLoggedInUser(session);
            ArticleDto articleDto = articleService.authorize(userPublicInfoDto, articleId);
            model.addAttribute("article", articleDto);
            return "article-edit";
        } catch (UserAuthorizationException e) {
            return "redirect:/articles/" + articleId;
        }
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto, HttpSession session) {
        ArticleDto savedArticleDto = articleService.save(loginChecker.getLoggedInUser(session), articleDto);
        return "redirect:/articles/" + savedArticleDto.getId();
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable("articleId") long articleId, ArticleDto articleDto, HttpSession session) {
        articleService.update(articleId, loginChecker.getLoggedInUser(session), articleDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") long id, HttpSession session) {
        articleService.delete(id, loginChecker.getLoggedInUser(session).getId());
        return "redirect:/";
    }
}
