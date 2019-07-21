package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.repository.ArticleRepository;

import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/articles")
@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public RedirectView createArticle(@Valid ArticleDto articleDto) {
        Article savedArticle = articleRepository.save(articleDto.toArticle());
        return new RedirectView("/articles/" + savedArticle.getId());
    }

    @GetMapping("/{articleId}")
    public String showArticle(@PathVariable long articleId, Model model) {
        Optional<Article> article = articleRepository.findById(articleId);
        article.ifPresent(value -> model.addAttribute("article", value));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticleForm(@PathVariable long articleId, Model model) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        articleOpt.ifPresent(value -> model.addAttribute("article", value));

        return "article-edit";
    }

    @PutMapping("/{articleId}")
    @Transactional
    public RedirectView editArticle(@PathVariable long articleId, @Valid ArticleDto articleDto) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        articleOpt.ifPresent(value -> value.update(articleDto.toArticle()));

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        articleOpt.ifPresent(articleRepository::delete);
        return new RedirectView("/");
    }
}
