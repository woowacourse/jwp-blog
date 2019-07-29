package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.Comment;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.service.CommentService;
import techcourse.myblog.web.LoginUser;

import javax.validation.Valid;
import java.util.List;

@RequestMapping("/articles")
@Controller
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

    //todo: BindException 처리 (ControllerAdvice 분할..?)
    @PostMapping("/write")
    public RedirectView createArticle(LoginUser loginUser, @Valid ArticleDto articleDto) {
        articleDto.setAuthor(loginUser.getUser());
        Article savedArticle = articleWriteService.save(articleDto.toArticle());
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
    public String editArticleForm(LoginUser loginUser, @PathVariable Long articleId, Model model) {
        Article article = articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(LoginUser loginUser, @PathVariable Long articleId, @Valid ArticleDto articleDto) {
        articleDto.setAuthor(loginUser.getUser());
        articleWriteService.update(articleId, articleDto);

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(LoginUser loginUser, @PathVariable Long articleId) {
        articleReadService.findByIdAndAuthor(articleId, loginUser.getUser());  //todo : 리턴값 활용하지 않음..
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }
}