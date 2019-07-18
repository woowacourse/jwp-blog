package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.dto.ArticleRequestDto;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static techcourse.myblog.web.ControllerUtil.SESSION_USER_KEY;
import static techcourse.myblog.web.ControllerUtil.checkAndPutUser;

@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String indexView(Model model, HttpSession session) {
        checkAndPutUser(model, session);
        List<Article> articles = new ArrayList<>();
        articleService.findAll().forEach(articles::add);
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String writeArticleView(Model model, HttpSession session) {
        model.addAttribute(SESSION_USER_KEY, session.getAttribute(SESSION_USER_KEY));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String publishArticle(ArticleRequestDto article) {
        Article saved = articleService.save(Article.from(article));
        return "redirect:/articles/" + saved.getId();
    }

    @GetMapping("/articles/{articleId}")
    public String articleView(@PathVariable Long articleId, Model model) {
        try {
            Article article = articleService.findById(articleId);
            model.addAttribute("article", article);
            return "article";
        } catch (NoSuchElementException e) {
            return "redirect:/";
        }
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticleView(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable Long articleId, ArticleRequestDto reqArticle, Model model) {
        Article article = Article.of(articleId, reqArticle.getTitle(), reqArticle.getCoverUrl(), reqArticle.getContents());
        articleService.save(article);
        Article articleToShow = articleService.findById(articleId);
        model.addAttribute("article", articleToShow);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
