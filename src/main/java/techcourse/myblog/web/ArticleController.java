package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.exception.CouldNotFindArticleIdException;

import javax.servlet.http.HttpSession;

import static techcourse.myblog.web.ControllerUtil.putLoginUser;

@Controller
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("/write")
    public String createArticle(ArticleDto articleDto) {
        Article article = articleService.save(articleDto);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/articles/new")
    public String editNewArticleView(HttpSession session, Model model) {
        putLoginUser(session, model);
        articleService.setActionOfArticle(model,
                "/write",
                "post"
        );

        return "article-edit";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticleView(@PathVariable Long articleId, HttpSession session, Model model) {
        putLoginUser(session, model);
        Article findArticle = articleService.findArticleById(articleId);
        articleService.setActionOfArticle(model,
                "/articles/" + articleId,
                "put"
        );

        model.addAttribute("article", findArticle);
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String searchArticleView(@PathVariable Long articleId, HttpSession session, Model model) {
        try {
            putLoginUser(session, model);
            Article findArticle = articleService.findArticleById(articleId);
            model.addAttribute("article", findArticle);
            return "article";
        } catch (CouldNotFindArticleIdException e) {
            return "redirect:/";
        }
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto articleDto) {
        Article updateArticle = articleService.update(articleId, articleDto);

        return "redirect:/articles/" + updateArticle.getArticleId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleService.deleteById(articleId);

        return "redirect:/";
    }
}
