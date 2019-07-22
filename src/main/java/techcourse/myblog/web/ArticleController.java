package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
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
        return new RedirectView("/articles/" + articleService.write(title, coverUrl, contents));
    }

    @GetMapping("/articles/{articleId}")
    public String read(@PathVariable long articleId, Model model) {
        return articleService.tryRender(articleId, model) ? "article" : "redirect:/";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable long articleId, Model model) {
        return articleService.tryRender(articleId, model) ? "article-edit" : "redirect:/";
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
}