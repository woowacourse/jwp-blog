package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

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
    public RedirectView createArticle(ArticleDto article) {
        Article savedArticle = articleRepository.save(article.toArticle());
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
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public RedirectView editArticle(@PathVariable long articleId, ArticleDto editedArticle) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        articleOpt.ifPresent(value -> {
            value.update(editedArticle.toArticle());
            articleRepository.save(value);
        });

        return new RedirectView("/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public RedirectView deleteArticle(@PathVariable long articleId) {
        Optional<Article> articleOpt = articleRepository.findById(articleId);
        articleOpt.ifPresent(articleRepository::delete);
        return new RedirectView("/");
    }
}
