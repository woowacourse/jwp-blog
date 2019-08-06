package techcourse.myblog.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.annotation.LoginUser;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
<<<<<<< HEAD
    private static final String USER = "user";
    private static final String ARTICLE = "article";

    private final ArticleService articleService;
    private final CommentService commentService;

    @Autowired
    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
=======
    private static final String ARTICLE = "article";

    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
>>>>>>> pair/younghyeon
    }

    @PostMapping("")
    public String createArticle(@Valid ArticleDto newArticleDto, @LoginUser User loginUser) {
        Article article = articleService.save(newArticleDto.toEntity(loginUser));
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}")
    public String selectArticle(@PathVariable long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute(ARTICLE, article);
<<<<<<< HEAD
        model.addAttribute("comments", commentService.findByArticle(article));
=======
>>>>>>> pair/younghyeon
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String moveArticleEditPage(@PathVariable long articleId, Model model, @LoginUser User loginUser) {
        Article article = articleService.findById(articleId, loginUser.getId());
        model.addAttribute(ARTICLE, article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable long articleId, @Valid ArticleDto updateArticleDto, @LoginUser User loginUser, Model model) {
        Article updateArticle = articleService.update(articleId, updateArticleDto.toEntity(loginUser));
        model.addAttribute(ARTICLE, updateArticle);
        return "redirect:/articles/" + updateArticle.getId();
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable long articleId, @LoginUser User loginUser) {
        Article article = articleService.findById(articleId, loginUser.getId());
        articleService.deleteById(article.getId());
        return "redirect:/";
    }
}
