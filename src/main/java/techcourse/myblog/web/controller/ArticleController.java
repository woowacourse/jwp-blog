package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.web.controller.dto.ArticleDto;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String goIndex(@RequestParam(defaultValue = "1") int page, Model model) {
        model.addAttribute("articles", articleService.findAll(page));
        return "index";
    }

    @GetMapping("/articles")
    public String createForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String show(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String create(ArticleDto articleDto) {
        log.debug("Create article : {}", articleDto);
        return "redirect:/articles/" + articleService.save(articleDto).getId();
    }

    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable Long articleId, ArticleDto articleDto) {
        articleService.update(articleId, articleDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String delete(@PathVariable Long articleId) {
        log.debug("Delete articleId : {}", articleId);
        articleService.delete(articleId);
        return "redirect:/";
    }
}
