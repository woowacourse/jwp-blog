package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.dto.CommentDto;
import techcourse.myblog.service.ArticleService;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public String showWritingForm() {
        return "article-edit";
    }

    @PostMapping
    public String writeArticle(ArticleDto articleDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Article article = articleService.save(articleDto, user);
        long articleId = article.getArticleId();
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        ArticleDto articleDto = articleService.getArticleDtoById(articleId);
        model.addAttribute("articleDto", articleDto);
        model.addAttribute("commentDtos", articleService.getAllComments(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String showEditForm(@PathVariable long articleId, Model model, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        Article article = articleService.getArticleById(articleId);
        article.checkCorrespondingAuthor(user);
        ArticleDto articleDto = articleService.getArticleDtoById(articleId);
        model.addAttribute("articleDto", articleDto);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto articleDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.update(articleDto, user);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable long articleId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.deleteById(articleId, user);
        return "redirect:/";
    }

    @PostMapping("/{articleId}/comment/new")
    public String createComment(@PathVariable long articleId, CommentDto commentDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.saveComment(articleId, commentDto, user);
        return "redirect:/articles/" + articleId;
    }

    @PutMapping("/{articleId}/comment/{commentId}")
    public String updateComment(@PathVariable long articleId, @PathVariable long commentId,
                                CommentDto commentDto, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.updateComment(commentId, commentDto, user);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}/comment/{commentId}")
    public String deleteComment(@PathVariable Long articleId, @PathVariable Long commentId, HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        articleService.deleteComment(commentId, user);
        return "redirect:/articles/" + articleId;
    }
}
