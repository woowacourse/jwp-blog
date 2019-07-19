package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repo.ArticleRepository;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;

    ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/writing")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto, Model model) {
        Article article = Article.builder()
                .title(articleDto.getTitle())
                .coverUrl(articleDto.getCoverUrl())
                .contents(articleDto.getContents())
                .build();
        articleRepository.save(article);
        model.addAttribute("article", article);
        return "redirect:/articles/" + article.getArticleId();
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String updateArticle(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @Transactional
    @PutMapping("/{articleId}")
    public String showUpdatedArticle(@PathVariable Long articleId, ArticleDto updatedArticle, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        article.update(updatedArticle);
        model.addAttribute("article", updatedArticle);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable Long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}
