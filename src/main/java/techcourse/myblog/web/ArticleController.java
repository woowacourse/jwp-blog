package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.UserException;
import techcourse.myblog.service.ArticleService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/")
    public String showArticles(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String showArticleWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String writeArticle(ArticleDto articleDto, Model model) {
        Article newArticle = articleService.write(articleDto);
        model.addAttribute("article", newArticle);

        return "redirect:/articles/" + newArticle.getId();
    }

    @GetMapping("/articles/{articleId}/edit")
    public String showArticleEditingPage(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleService.findById(articleId));
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable int articleId, Model model) throws UserException {
        model.addAttribute("article", articleService.findById(articleId));
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable int articleId, ArticleDto articleDto, Model model) {
        Article updatedArticle = articleService.update(articleDto, articleId);
        model.addAttribute("article", updatedArticle);

        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleService.deleteById(articleId);
        return "redirect:/";
    }
}
