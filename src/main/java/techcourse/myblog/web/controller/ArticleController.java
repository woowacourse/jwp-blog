package techcourse.myblog.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleReadService;
import techcourse.myblog.service.ArticleWriteService;
import techcourse.myblog.web.LoginUser;

import javax.validation.Valid;

@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleReadService articleReadService;
    private final ArticleWriteService articleWriteService;

    public ArticleController(ArticleReadService articleReadService, ArticleWriteService articleWriteService) {
        this.articleReadService = articleReadService;
        this.articleWriteService = articleWriteService;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(LoginUser loginUser, @Valid ArticleDto articleDto) {
        articleDto.setAuthor(loginUser.getUser());
        Article savedArticle = articleWriteService.save(articleDto.toArticle());
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Article article = articleReadService.findById(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(LoginUser loginUser, @PathVariable Long articleId, Model model) {
        Article article = articleWriteService.findByIdAndAuthor(articleId, loginUser.getUser());
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
        articleWriteService.findByIdAndAuthor(articleId, loginUser.getUser());  //todo : 리턴값 활용하지 않음..
        articleWriteService.removeById(articleId);
        return new RedirectView("/");
    }
}