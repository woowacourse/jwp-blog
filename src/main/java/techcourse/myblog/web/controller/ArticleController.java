package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    public ArticleController(ArticleReadService articleReadService, ArticleWriteService articleWriteService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(@Valid ArticleDto articleDto) {
        Article savedArticle = articleWriteService.save(articleDto.toArticle());
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Optional<Article> article = articleReadService.findById(articleId);
        article.ifPresent(value -> model.addAttribute("article", value));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable long articleId, Model model) {
        Optional<Article> articleOpt = articleReadService.findById(articleId);
        articleOpt.ifPresent(value -> model.addAttribute("article", value));

        return "article-edit";
    }

    @PutMapping("/{articleId}")
    @Transactional
    public RedirectView editArticle(@PathVariable Long articleId, @Valid ArticleDto articleDto) {
        articleWriteService.update(articleId, articleDto);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable Long articleId) {
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }
}