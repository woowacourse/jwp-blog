package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.user.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.UserService;
import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final UserService userService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    private User getCurrentUser(HttpSession session) {
        return userService.getUserByEmail((String) session.getAttribute("email"));
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.loadEveryArticles());
        return "index";
    }

    @GetMapping("/writing")
    public String writingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView write(String title, String coverUrl, String contents, HttpSession session) {
        return new RedirectView("/articles/" + articleService.write(
                new Article(getCurrentUser(session), title, coverUrl, contents))
        );
    }

    @GetMapping("/articles/{articleId}")
    public String read(@PathVariable long articleId, Model model) {
        return articleService.maybeArticle(articleId).map(article -> {
            model.addAttribute("article", article);
            model.addAttribute("comments", article.getComments());
            return "article";
        }).orElse("redirect:/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable long articleId, Model model, HttpSession session) {
        return articleService.maybeArticle(articleId).map(article -> {
            if (article.isSameAuthor(getCurrentUser(session))) {
                model.addAttribute("article", article);
                return "article-edit";
            }
            return "redirect:/";
        }).orElse("redirect:/");
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView update(
            @PathVariable long articleId,
            String title,
            String coverUrl,
            String contents,
            HttpSession session
    ) {
        articleService.tryUpdate(articleId, new Article(getCurrentUser(session), title, coverUrl, contents));
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable long articleId, HttpSession session) {
        articleService.tryDelete(articleId, getCurrentUser(session));
        return new RedirectView("/");
    }
}