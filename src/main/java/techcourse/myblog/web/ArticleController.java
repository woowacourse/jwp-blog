package techcourse.myblog.web;

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
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleService;
import techcourse.myblog.dto.ArticleDto;

@RequestMapping("/articles")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(@Valid ArticleDto articleDto) {
        Article article = articleService.save(articleDto.toArticle());
        return new RedirectView("/articles/" + article.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Article article = articleService.select(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable long articleId, Model model) {
        Article article = articleService.select(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    @Transactional
    public RedirectView editArticle(@PathVariable long articleId, @Valid ArticleDto articleDto) {
        articleService.update(articleId, articleDto.toArticle());
        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId) {
        articleService.delete(articleId);
        return new RedirectView("/");
    }
}
