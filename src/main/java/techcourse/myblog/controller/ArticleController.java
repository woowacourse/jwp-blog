package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.service.dto.ArticleDto;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleWriteService articleWriteService;
    private final ArticleReadService articleReadService;

    public ArticleController(final ArticleWriteService articleWriteService,
                             final ArticleReadService articleReadService) {
        this.articleWriteService = articleWriteService;
        this.articleReadService = articleReadService;
    }

    @PostMapping
    public String createArticle(@Valid ArticleDto articleDto) {
        Article article = articleWriteService.save(articleDto);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}")
    public String selectArticle(@PathVariable Long articleId, Model model) {
        Optional<Article> articleOptional = articleReadService.findById(articleId);
        if (articleOptional.isPresent()) {
            model.addAttribute("article", articleOptional.get());
            return "article";
        }
        return "redirect:/";
    }

    @GetMapping("/{articleId}/edit")
    public String updateArticleForm(@PathVariable Long articleId, Model model) {
        Optional<Article> articleOptional = articleReadService.findById(articleId);
        if (articleOptional.isPresent()) {
            model.addAttribute("article", articleOptional.get());
            return "article-edit";
        }
        return "redirect:/";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, @Valid ArticleDto articleDto) {
        articleWriteService.update(articleId, articleDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleWriteService.deleteById(articleId);
        return "redirect:/";
    }
}
