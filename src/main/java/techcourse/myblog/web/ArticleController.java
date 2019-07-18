package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.domain.validator.CouldNotFindArticleIdException;
import techcourse.myblog.web.dto.ArticleDto;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

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
    public String articleEditPage(@PathVariable Long articleId, Model model) {
        Article article = articleRepository
                .findById(articleId)
                .orElseThrow(CouldNotFindArticleIdException::new);
        String actionRoute = "/articles/" + articleId;
        String formMethod = "put";

        model.addAttribute("actionRoute", actionRoute);
        model.addAttribute("formMethod", formMethod);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PostMapping("/write")
    public String createNewArticle(ArticleDto articleDto) {
        Article article = articleRepository.save(new Article(articleDto));
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/articles/{articleId}")
    public String searchArticle(@PathVariable Long articleId, Model model) {
        Article article = articleRepository
                .findById(articleId)
                .orElseThrow(CouldNotFindArticleIdException::new);

        model.addAttribute("article", article);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable Long articleId, ArticleDto articleDto) {
        articleRepository.findById(articleId)
                .orElseThrow(CouldNotFindArticleIdException::new)
                .updateArticle(articleDto);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteById(articleId);

        return "redirect:/";
    }
}
