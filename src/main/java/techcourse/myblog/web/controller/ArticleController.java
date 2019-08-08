package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.web.exception.CreateArticleBindException;
import techcourse.myblog.web.support.SessionUser;

import javax.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    public ArticleController(ArticleReadService articleReadService,
                             ArticleWriteService articleWriteService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(SessionUser loginUser,
                                      @Valid ArticleDto articleDto,
                                      BindingResult bindingResult) throws CreateArticleBindException {
        if (bindingResult.hasErrors()) {
            throw new CreateArticleBindException(bindingResult);
        }

        Article savedArticle = articleWriteService.save(articleDto.toArticle(loginUser.getUser()));
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Article article = articleReadService.findById(articleId);
        model.addAttribute("article", article);
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
}