package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.dto.ArticleDto;

import javax.servlet.http.HttpSession;

@Controller
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/articles")
    public String createForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String show(@PathVariable Long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", commentService.findByArticle(article));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateForm(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @PostMapping("/articles")
    public String create(ArticleDto articleDto, HttpSession httpSession) {
        articleDto.setAuthor((User) httpSession.getAttribute("user"));
        log.debug("articleDto : {}", articleDto);
        return "redirect:/articles/" + articleService.save(articleDto).getId();
    }

    @PutMapping("/articles/{articleId}")
    public String update(@PathVariable Long articleId, ArticleDto articleDto) {
        articleService.update(articleId, articleDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String delete(@PathVariable Long articleId) {
        log.debug("articleId : {}", articleId);
        articleService.delete(articleId);
        return "redirect:/";
    }
}
