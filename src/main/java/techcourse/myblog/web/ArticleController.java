package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.ArticleRequest;
import techcourse.myblog.service.dto.CommentsResponse;
import techcourse.myblog.support.validator.UserSession;

import javax.validation.Valid;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private ArticleService articleService;
    private CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/articles/edit")
    public String formArticle(Model model) {
        log.debug("begin");

        model.addAttribute("article", null);
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(@Valid ArticleRequest articleRequest, Model model, @UserSession User sessionUser) {
        log.debug("begin");

        Article article = articleService.save(articleRequest, sessionUser);
        log.info("article: {}", article);

        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{articleId}")
    public String showArticlePage(@PathVariable("articleId") long articleId, Model model) {
        log.debug("begin");

        model.addAttribute("articleId", articleId);
        return "article";
    }

    @GetMapping("/api/articles/{articleId}")
    @ResponseBody
    public ResponseEntity<Article> selectArticle(@PathVariable("articleId") long articleId) {
        log.debug("begin");

        return new ResponseEntity<>(articleService.findById(articleId), HttpStatus.OK);
    }


    @GetMapping("/articles/{articleId}/edit")
    public String edit(@PathVariable("articleId") long articleId, Model model, @UserSession User sessionUser) {
        log.debug("begin");

        Article article = articleService.findByIdWithUser(articleId, sessionUser);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable("articleId") long articleId, @ModelAttribute ArticleRequest articleRequest,
                              @UserSession User sessionUser, Model model) {
        log.debug("begin");

        Article article = articleService.editArticle(articleRequest, articleId, sessionUser);
        model.addAttribute("article", article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable("articleId") long articleId, @UserSession User sessionUser) {
        log.debug("begin");

        articleService.deleteById(articleId, sessionUser);
        log.info("articleId: {}", articleId);

        return "redirect:/";
    }

    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentsResponse> getComment(@PathVariable("articleId") long articleId) {
        log.debug("begin");

        return new ResponseEntity<>(commentService.findByArticleId(articleId), HttpStatus.OK);
    }
}
