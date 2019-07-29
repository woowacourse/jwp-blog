package techcourse.myblog.article.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.argumentresolver.UserSession;
import techcourse.myblog.article.dto.ArticleDto;
import techcourse.myblog.article.exception.NotMatchUserException;
import techcourse.myblog.article.service.ArticleService;

@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Creation articleDto, UserSession userSession) {
        long newArticleId = articleService.save(articleDto, userSession.getId());
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, Model model, UserSession userSession) {
        ArticleDto.Response articleResponse = articleService.findById(articleId);
        if (!articleResponse.matchAuthorId(userSession.getId())) {
            throw new NotMatchUserException();
        }
        model.addAttribute("article", articleResponse);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto.Updation articleDto, UserSession userSession) {
        long updatedArticleId = articleService.update(articleId, articleDto, userSession.getId());
        return "redirect:/articles/" + updatedArticleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId, UserSession userSession) {
        articleService.deleteById(articleId, userSession.getId());
        return "redirect:/";
    }
}
