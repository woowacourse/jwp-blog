package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

import techcourse.myblog.controller.argumentresolver.UserSession;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

@RequestMapping("/articles")
@Controller
public class ArticleController {

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(@Valid @ModelAttribute ArticleDto articleDto,
                                      UserSession userSession) {
        Article article = articleService.save(articleDto.toDomain(userSession.getUser()));
        return new RedirectView("/articles/" + article.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.select(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.select(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(@PathVariable Long articleId,
                                    @Valid ArticleDto articleDto,
                                    UserSession userSession) {
        articleService.update(articleId, articleDto.toDomain(), userSession.getUser());
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId,
                                      UserSession userSession) {
        articleService.delete(articleId, userSession.getUser());
        return new RedirectView("/");
    }
}
