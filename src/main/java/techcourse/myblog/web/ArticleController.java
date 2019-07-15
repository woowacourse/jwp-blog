package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.web.dto.ArticleDto;

@Controller
public class ArticleController {
    private static final int INCREMENT_AMOUNT = 1;

    private ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/articles/new")
    public String articleCreationPage(Model model) {
        String actionRoute = "/write";
        String formMethod = "post";

        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String articleEditPage(@PathVariable int articleId, Model model) {
        Article article = articleRepository.find(articleId);
        String actionRoute = "/articles/" + articleId;
        String formMethod = "put";

        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PostMapping("/write")
    public String createNewArticle(ArticleDto articleDto) {
        int articleId = articleRepository.getLastArticleId();
        Article article = new Article(articleId, articleDto);

        articleRepository.save(article);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String searchArticle(@PathVariable int articleId, Model model) {
        Article article = articleRepository.find(articleId);

        model.addAttribute("article", article);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable int articleId, String title, String coverUrl, String contents) {
        articleRepository.updateTitle(articleId, title);
        articleRepository.updateCoverUrl(articleId, coverUrl);
        articleRepository.updateContents(articleId, contents);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.delete(articleId);

        return "redirect:/";
    }
}
