package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.LoginUser;
import techcourse.myblog.application.service.ArticleService;
import techcourse.myblog.application.service.UserService;
import techcourse.myblog.domain.Article;

@Controller
public class ArticleController {
    private final UserService userService;
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService, UserService userService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model, LoginUser sessionUser) {
        model.addAttribute("session", sessionUser.getUser());
        model.addAttribute("articles", articleService.loadEveryArticles());
        return "index";
    }

    @GetMapping("/writing")
    public String writingForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView write(String title,
                              String coverUrl,
                              String contents,
                              LoginUser sessionUser
    ) {
        return new RedirectView("/articles/" + articleService.write(
                new Article(sessionUser.getUser(), title, coverUrl, contents))
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
    public String updateForm(@PathVariable long articleId, Model model, LoginUser sessionUser) {
        return articleService.maybeArticle(articleId).map(article -> {
            if (article.isSameAuthor(sessionUser.getUser())) {
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
            LoginUser sessionUser
    ) {
        return articleService.tryUpdate(articleId, new Article(sessionUser.getUser(), title, coverUrl, contents))
                ? new RedirectView("/articles/" + articleId)
                : new RedirectView("/");
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable long articleId, LoginUser sessionUser) {
        articleService.tryDelete(articleId, sessionUser.getUser());
        return new RedirectView("/");
    }

}