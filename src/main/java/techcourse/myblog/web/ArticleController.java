package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.ArticleException;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.info("START {} ", model);
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        try {
            model.addAttribute("article", articleService.findArticle(articleId));
            return "article";
        } catch (ArticleException e) {
            return "/";

        }
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.findArticle(articleId));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(ArticleDto dto) {
        return "redirect:/articles/" + articleService.save(dto);
    }

    @PutMapping("/articles/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId, ArticleDto dto, Model model) {
        model.addAttribute("article", articleService.update(articleId, dto));
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return "redirect:/";
    }
}