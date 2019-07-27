package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

import lombok.RequiredArgsConstructor;
import techcourse.myblog.controller.session.UserSession;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.CommentService;

@RequestMapping("/articles")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(@Valid ArticleDto articleDto, UserSession userSession) {
        Article article = articleService.save(articleDto.toDomain(), userSession.getUser());
        return new RedirectView("/articles/" + article.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.select(articleId));
        model.addAttribute("comments", commentService.findAll(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable long articleId, Model model) {
        model.addAttribute("article", articleService.select(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    @Transactional
    public RedirectView editArticle(@PathVariable long articleId, @Valid ArticleDto articleDto) {
        articleService.update(articleId, articleDto.toDomain());
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return new RedirectView("/");
    }
}
