package techcourse.myblog.web.controller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.LoginUser;
import techcourse.myblog.application.service.ArticleService;
import techcourse.myblog.domain.Article;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model, LoginUser loginUser) {
        model.addAttribute("user", loginUser.getUser());
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
                              LoginUser loginUser
    ) {
        return new RedirectView("/articles/" + articleService.write(
                new Article(loginUser.getUser(), title, coverUrl, contents)).getId()
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
    public String updateForm(@PathVariable long articleId, Model model, LoginUser loginUser) {
        return articleService.maybeArticle(articleId).map(article -> {
            if (article.isSameAuthor(loginUser.getUser())) {
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
            LoginUser loginUser
    ) {
        return articleService.tryUpdate(articleId, new Article(loginUser.getUser(), title, coverUrl, contents))
                ? new RedirectView("/articles/" + articleId)
                : new RedirectView("/");
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable long articleId, LoginUser loginUser) {
        articleService.tryDelete(articleId, loginUser.getUser());
        return new RedirectView("/");
    }

}