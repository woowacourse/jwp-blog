package techcourse.myblog.presentation;

import javafx.scene.canvas.GraphicsContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.ArticleRepository;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.exception.ArticleNotFoundException;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;

import static techcourse.myblog.service.UserService.LOGGED_IN_USER_SESSION_KEY;

@Slf4j
@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final CommentRepository commentRepository;

    public ArticleController(ArticleRepository articleRepository, ArticleService articleService, CommentRepository commentRepository) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String addNewArticle(ArticleRequestDto articleRequestDto, HttpSession session) {
        User user = (User) session.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        Article newArticle = articleRequestDto.toArticle();
        newArticle.setAuthor(user);
        articleRepository.save(newArticle);
        return "redirect:/articles/" + newArticle.getId();
    }

    @GetMapping("/")
    public String showArticlesPage(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable long articleId, Model model) {
        model.addAttribute("article",
                articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new));
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleByIdPage(@PathVariable long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        model.addAttribute("article", article);

        List<CommentResponseDto> comments =  commentRepository.findByArticleOrderByCreatedAt(article)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        model.addAttribute("comments", comments);
        return "article";
    }

    @PutMapping("/articles")
    public String updateArticleById(ArticleRequestDto articleRequestDto) {
        articleService.update(articleRequestDto);
        return "redirect:/articles/" + articleRequestDto.getId();
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticleById(@PathVariable long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }

    @PostMapping("/articles/{articleId}/comments")
    public String addNewComment(@PathVariable long articleId, CommentRequestDto commentRequestDto, HttpSession httpSession) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        User commenter = (User) httpSession.getAttribute(LOGGED_IN_USER_SESSION_KEY);
        Comment newComment = commentRequestDto.toComment(commenter, article);
        commentRepository.save(newComment);
        return "redirect:/articles/" + articleId;
    }
}
