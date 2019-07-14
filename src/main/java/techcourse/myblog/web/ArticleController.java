package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleNotFoundException;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping(value = {"/", "/articles"})
    public String showArticles(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDto) {
        Article addedArticle = articleRepository.add(articleDto);
        return "redirect:/articles/" + addedArticle.getId();
    }

    @GetMapping("/articles/new")
    public String showCreatePage() {
        return "article-edit";
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable("id") String id, Model model) {
        articleRepository.findById(Long.parseLong(id))
                .map(foundArticle -> model.addAttribute("article", foundArticle))
                .orElseThrow(ArticleNotFoundException::new);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable("id") String id, Model model) {
        articleRepository.findById(Long.parseLong(id))
                .map(foundArticle -> model.addAttribute("article", foundArticle))
                .orElseThrow(ArticleNotFoundException::new);
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String editArticle(@PathVariable("id") String id, ArticleDto articleDto) {
        articleRepository.findById(Long.parseLong(id))
                .ifPresent(article -> article.updateArticle(articleDto));
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable("id") String id) {
        articleRepository.deleteById(Long.parseLong(id));
        return "redirect:/";
    }
}
