package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.article.Article;
import techcourse.myblog.article.ArticleRepository;
import techcourse.myblog.exception.NotFoundObjectException;
import techcourse.myblog.service.dto.ArticleDto;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

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
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundObjectException::new);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String updateArticle(@PathVariable Long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundObjectException::new);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @Transactional
    @PutMapping("/{articleId}")
    public String showUpdatedArticle(@PathVariable Long articleId, ArticleDto updatedArticle, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(NotFoundObjectException::new);
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
