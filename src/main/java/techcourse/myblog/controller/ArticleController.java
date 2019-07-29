package techcourse.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.exception.NotFoundArticleException;
import techcourse.myblog.repository.ArticleRepository;
import techcourse.myblog.service.dto.ArticleDTO;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable("articleId") Long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(() ->
                new NotFoundArticleException("게시물이 없습니다"));

        model.addAttribute("article", article);
        model.addAttribute("comments", article.getComments());
        model.addAttribute("articleId", articleId);

        return "article";
    }

    @GetMapping("/new")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String saveArticle(ArticleDTO articleDTO) {
        Article article = articleRepository.save(articleDTO.toDomain());
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{articleId}/edit")
    public String articleUpdateForm(@PathVariable("articleId") Long articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId).get());
        model.addAttribute("articleId", articleId);
        return "article-edit";
    }

    @Transactional
    @PutMapping("/{articleId}")
    public String updateArticle(@PathVariable("articleId") Long articleId, ArticleDTO articleDTO) {
        Article article = articleRepository.findById(articleId).get();
        article.update(articleDTO.toDomain());

        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable("articleId") Long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}