package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article-edit";
    }

    @PostMapping
    public String saveArticle(ArticleDto dto) {
        return "redirect:/articles/" + articleService.save(dto.toEntity());
    }

    @PutMapping("/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId, ArticleDto dto, Model model) {
        setArticleModel(model, articleService.update(articleId, dto));
        return "article";
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return "redirect:/";
    }

    private void setArticleModel(Model model, Article article) {
        model.addAttribute("article", article);
    }
}