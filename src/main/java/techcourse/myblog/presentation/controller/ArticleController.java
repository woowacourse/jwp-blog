package techcourse.myblog.presentation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.comment.Comment;
import techcourse.myblog.application.ArticleReadService;
import techcourse.myblog.application.ArticleWriteService;
import techcourse.myblog.application.CommentReadService;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.presentation.support.LoginUser;

import javax.validation.groups.Default;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;
    private final CommentReadService commentReadService;

    public ArticleController(ArticleReadService articleReadService,
                             ArticleWriteService articleWriteService,
                             CommentReadService commentReadService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
        this.commentReadService = commentReadService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(LoginUser loginUser, @ModelAttribute("/articles/writing") @Validated(Default.class) ArticleDto articleDto) {
        Article savedArticle = articleWriteService.save(articleDto.toArticle(loginUser.getUser()));
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Article article = articleReadService.findById(articleId);
        List<Comment> comments = commentReadService.findByArticleId(articleId);
        model.addAttribute("article", article);
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(LoginUser loginUser, @PathVariable Long articleId, Model model) {
        Article article = articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(LoginUser loginUser, @PathVariable Long articleId, @ModelAttribute("/") @Validated(Default.class) ArticleDto articleDto) {
        articleWriteService.update(articleId, articleDto.toArticle(loginUser.getUser()));

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(LoginUser loginUser, @PathVariable Long articleId) {
        articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }

    @ExceptionHandler(BindException.class)
    public RedirectView handleBindError(BindException e) {
        return new RedirectView(e.getObjectName());
    }
}