package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(final ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/writing")
    public String articleCreateForm() {
        return "article-edit";
    }

    @PostMapping
    public ModelAndView writeArticle(Article article, Model model) {
        articleRepository.create(article);
        model.addAttribute("article", articleRepository.findLastest());
        return new ModelAndView("redirect:/articles/" + article.getId());
    }

    @GetMapping("/{articleId}")
    public String viewArticle(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article";
    }

    @GetMapping("/{articleId}/edit")
    public String editArticle(@PathVariable int articleId, Model model) {
        model.addAttribute("article", articleRepository.findById(articleId));
        return "article-edit";
    }

    @PutMapping("/{articleId}")
    public ModelAndView editArticle(@PathVariable int articleId, Article article) {
        articleRepository.modify(articleId, article);
        return new ModelAndView("redirect:/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.delete(articleId);
        return "redirect:/";
    }
}
