package techcourse.myblog.presentation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.domain.User;
import techcourse.myblog.persistence.ArticleRepository;
import techcourse.myblog.persistence.CommentRepository;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.dto.CommentRequestDto;
import techcourse.myblog.service.dto.CommentResponseDto;
import techcourse.myblog.service.exception.ArticleNotFoundException;
import techcourse.myblog.web.SessionUser;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;
    private final ArticleService articleService;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    public ArticleController(ArticleRepository articleRepository, ArticleService articleService, CommentRepository commentRepository, CommentService commentService) {
        this.articleRepository = articleRepository;
        this.articleService = articleService;
        this.commentRepository = commentRepository;
        this.commentService = commentService;
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public RedirectView addNewArticle(ArticleRequestDto articleRequestDto, @SessionUser User loggedInUser) {
        Article newArticle = articleRequestDto.toArticle();
        newArticle.setAuthor(loggedInUser);
        articleRepository.save(newArticle);

        return new RedirectView("/articles/" + newArticle.getId());
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

        List<CommentResponseDto> comments = commentRepository.findByArticleOrderByCreatedAt(article)
                .stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
        model.addAttribute("comments", comments);
        return "article";
    }

    @PutMapping("/articles")
    public RedirectView updateArticleById(ArticleRequestDto articleRequestDto) {
        articleService.update(articleRequestDto);

        return new RedirectView("/articles/" + articleRequestDto.getId());
    }

    @DeleteMapping("/articles/{articleId}")
    public RedirectView deleteArticleById(@PathVariable long articleId) {
        commentService.deleteByArticle(articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new));
        articleRepository.deleteById(articleId);

        return new RedirectView("/");
    }

    @PostMapping("/articles/{articleId}/comments")
    public RedirectView addNewComment(@PathVariable long articleId, CommentRequestDto commentRequestDto, @SessionUser User loggedInUser) {
        Article article = articleRepository.findById(articleId).orElseThrow(ArticleNotFoundException::new);
        Comment newComment = commentRequestDto.toComment(loggedInUser, article);
        commentRepository.save(newComment);

        return new RedirectView("/articles/" + articleId);
    }
}
