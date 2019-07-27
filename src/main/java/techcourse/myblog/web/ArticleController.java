package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.UserSessionInfo;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }


    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article";
    }

    @PostMapping("/articles/{articleId}/comment")
    public String addComment(@PathVariable long articleId, UserSessionInfo userSessionInfo, CommentDto commentDto) {
        commentService.addComment(articleId, userSessionInfo.getEmail(), commentDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable long articleId, @PathVariable long commentId, UserSessionInfo userSessionInfo) {
        commentService.deleteComment(commentId, userSessionInfo.getEmail());
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        setArticleModel(model, articleService.findArticle(articleId));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(ArticleDto dto) {
        return "redirect:/articles/" + articleService.save(dto.toEntity());
    }

    @PutMapping("/articles/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId, ArticleDto dto, Model model) {
        setArticleModel(model, articleService.update(articleId, dto));
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return "redirect:/";
    }

    private void setArticleModel(Model model, Article article) {
        model.addAttribute("article", article);
    }
}