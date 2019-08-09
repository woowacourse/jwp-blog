package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.dto.CommentRequestDto;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import static techcourse.myblog.service.UserService.LOGGED_IN_USER_SESSION_KEY;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;
    private final CommentService commentService;

    public ArticleController(ArticleService articleService, CommentService commentService) {
        this.articleService = articleService;
        this.commentService = commentService;
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("")
    public String addNewArticle(@ModelAttribute("/articles/writing") @Valid ArticleRequestDto articleRequestDto, HttpSession session) {
        User user = (User) session.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        Article newArticle = articleRequestDto.toArticle();
        newArticle.setAuthor(user);
        Article persistArticle = articleService.save(newArticle);
        return "redirect:/articles/" + persistArticle.getId();
    }

    @GetMapping("")
    public String showArticlesPage(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "index";
    }

    @GetMapping("/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model) {
        model.addAttribute("article",
                articleService.findById(articleId));
    return "article-edit";
    }

    @GetMapping("/{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        Article article = articleService.findById(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments",
                commentService.commentsToDtos(commentService.findAllByArticle(article)));
        return "article";
    }

    @PutMapping("/{articleId}")
    public String updateArticleById(@PathVariable long articleId, ArticleRequestDto articleRequestDto) {
        articleService.update(articleId, articleRequestDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticleById(@PathVariable long articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }

    @PostMapping("/{articleId}/comments")
    public String addNewComment(@PathVariable long articleId, CommentRequestDto commentRequestDto, HttpSession httpSession) {
        Article article = articleService.findById(articleId);
        User commenter = (User) httpSession.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        Comment newComment = commentRequestDto.toComment(commenter, article);
        commentService.save(newComment);
        return "redirect:/articles/" + articleId;
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("article", e.getBindingResult().getTarget());
        redirectAttributes.addFlashAttribute("errorMsg", e.getAllErrors().get(0).getDefaultMessage());
        return new RedirectView(e.getObjectName());
    }
}
