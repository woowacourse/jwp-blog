package techcourse.myblog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.dto.ArticleDto;
import techcourse.myblog.domain.ArticleRepository;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;
    
    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto) {
        Article article = articleRepository.save(articleDto.toArticle());
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}")
    public String selectArticle(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String updateArticleForm(@PathVariable Long articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId).get());
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable Long articleId, ArticleDto articleDto) {
        articleRepository.update(articleId, articleDto);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
