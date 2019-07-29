package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.UserRepository;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.UserService;

import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;
    private final UserService userService;
    public ArticleController(ArticleService articleService, CommentService commentService, UserService userService) {
        this.userService = userService;
        this.articleService = articleService;
        this.commentService = commentService;
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
    public RedirectView write(String title, String coverUrl, String contents) {
        return new RedirectView("/articles/" + articleService.write(new Article(title, coverUrl, contents)));
    }

    @GetMapping("/articles/{articleId}")
    public String read(@PathVariable long articleId, Model model) {
        return articleService.maybeArticle(articleId).map(article -> {
            model.addAttribute("article", article);
//            model.addAttribute("comments", article.getComments());
            return "article";
        }).orElse("redirect:/");
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable long articleId, Model model) {
        return articleService.maybeArticle(articleId).map(article -> {
            model.addAttribute("article", article);
            return "article-edit";
        }).orElse("redirect:/");
    }

    @PutMapping("/articles/{articleId}")
    public RedirectView update(@PathVariable long articleId, String title, String coverUrl, String contents) {
        return articleService.tryUpdate(articleId, new Article(title, coverUrl, contents))
                ? new RedirectView("/articles/" + articleId)
                : new RedirectView("/");
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView delete(@PathVariable long articleId) {
        articleService.delete(articleId);
        return new RedirectView("/");
    }

    @PostMapping("/articles/{articleId}/comment")
    public RedirectView writeComment(@PathVariable long articleId, String contents, HttpSession session){
        String currentEmail = (String)session.getAttribute("email");
        commentService.write(articleService.maybeArticle(articleId), userService.getUserByEmail(currentEmail),contents);
        return
    }
}