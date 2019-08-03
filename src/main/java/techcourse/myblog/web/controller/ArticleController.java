package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.support.SessionUser;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;
    private final CommentService commentService;

    public ArticleController(ArticleReadService articleReadService,
                             ArticleWriteService articleWriteService,
                             CommentService commentService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
        this.commentService = commentService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(SessionUser loginUser, @ModelAttribute("/articles/writing") @Valid ArticleDto articleDto) {
        Article savedArticle = articleWriteService.save(articleDto.toArticle(loginUser.getUser()));
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Article article = articleReadService.findById(articleId);
        List<Comment> comments = commentService.findByArticleId(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(SessionUser loginUser, @PathVariable Long articleId, Model model) {
        Article article = articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(SessionUser loginUser, @PathVariable Long articleId, @ModelAttribute("/") @Valid ArticleDto articleDto) {
        articleWriteService.update(articleId, articleDto.toArticle(loginUser.getUser()));

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(SessionUser loginUser, @PathVariable Long articleId) {
        articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e) {
        return new RedirectView(e.getObjectName());
    }
}