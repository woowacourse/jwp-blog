package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.ArticleDto;
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
    public RedirectView writeArticle(ArticleDto articleDto, Model model) {
        int newId = articleRepository.getLatestIndex();
        Article persistArticle = articleRepository.create(articleDto.convertArticle(newId));
        model.addAttribute("article", persistArticle);
        return new RedirectView("redirect:/articles/" + newId);
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
    public RedirectView editArticle(@PathVariable int articleId, ArticleDto articleDto) {
        articleRepository.update(articleId, articleDto.convertArticle(articleId));
        return new RedirectView("redirect:/articles/" + articleId);
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.delete(articleId);
        return "redirect:/";
    }
}
