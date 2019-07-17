package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.*;
import techcourse.myblog.service.CategoryService;

import java.util.Optional;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/article/new")
    public String showWritingPage(Model model) {
        model.addAttribute("categories", categoryService.findAll());
        return "article-edit";
    }

    @PostMapping("/articles/new")
    public String addArticle(ArticleDto articleDto) {
        Article article = articleRepository.save(articleDto.toArticle());
        long latestId = article.getId();
        return "redirect:/articles/" + latestId;
    }

    @GetMapping("/articles/{articleId}")
    public String showArticleById(@PathVariable long articleId, Model model) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            model.addAttribute("article", ArticleDto.from(maybeArticle.get()));
            return "article";
        }
        return "error";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto articleDto) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            ArticleDto findArticleDto = ArticleDto.from(maybeArticle.get());
            articleDto.setId(findArticleDto.getId());
            articleRepository.save(articleDto.toArticle());
        }

        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}/edit")
    public String updateArticle(@PathVariable long articleId, Model model) {
        Optional<Article> maybeArticle = articleRepository.findById(articleId);
        if (maybeArticle.isPresent()) {
            model.addAttribute("article", ArticleDto.from(maybeArticle.get()));
        }
        model.addAttribute("categories", categoryService.findAll());
        return "article-edit";
    }

    @DeleteMapping("articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
