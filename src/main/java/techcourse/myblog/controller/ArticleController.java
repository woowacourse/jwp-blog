package techcourse.myblog.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.controller.dto.ArticleDto;
import techcourse.myblog.model.Article;
import techcourse.myblog.model.User;
import techcourse.myblog.service.ArticleService;

@Controller
@SessionAttributes("user")
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/writing")
    public String articleForm(){
        return "article-edit";
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto, @ModelAttribute User user){
        Long newArticleId = articleService.save(articleDto, user);
        return "redirect:/articles/" + newArticleId;
    }

    @GetMapping("/{articleId}")
    public String findArticle(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String articleEditForm(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto articleDto, @ModelAttribute User user) {
        articleDto.setId(articleId);
        articleService.update(articleDto, user);

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleService.delete(articleId);
        return "redirect:/";
    }

    @DeleteMapping
    public String deleteAllArticles(@ModelAttribute User user){
        articleService.deleteAll(user);
        return "redirect:/";
    }
}

