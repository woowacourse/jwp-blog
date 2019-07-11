package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/articles/new")
    public String articlesNew(Model model) {
        String actionRoute = "/write";
        String formMethod = "post";

        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
        return "article-edit";
    }

    @PostMapping("/write")
    public String createPosting(String title, String coverUrl, String contents, Model model) {
        Article article = new Article(title, coverUrl, contents, articleRepository.getLastArticleId());

        articleRepository.save(article);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}")
    public String searchArticle(@PathVariable int articleId, Model model) {
        Article article = articleRepository.find(articleId);

        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditPage(@PathVariable int articleId, Model model) {
        Article article = articleRepository.find(articleId);
        String actionRoute = "/articles/" + articleId;
        String formMethod = "put";

        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable int articleId, String title, String coverUrl, String contents, Model model) {
        articleRepository.updateTitle(articleId, title);
        articleRepository.updateCoverUrl(articleId, coverUrl);
        articleRepository.updateContents(articleId, contents);

        Article article = articleRepository.find(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId, Model model) {
        articleRepository.delete(articleId);
        List<Article> articles = articleRepository.findAll();

        model.addAttribute("articles", articles);
        return "index";
    }
}
