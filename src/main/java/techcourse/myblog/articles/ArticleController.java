package techcourse.myblog.articles;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping(ArticleController.ARTICLE_BASE_URI)
@RequiredArgsConstructor
public class ArticleController {
    public static final String ARTICLE_BASE_URI = "/articles";

    private final ArticleService articleService;

    @GetMapping("/new")
    public String writeForm() {
        return "article-edit";
    }

    @PostMapping
    public String write(Article article) {
        Article savedArticle = articleService.save(article);
        return "redirect:/articles/" + savedArticle.getId();
    }

    @GetMapping("/{id}")
    public String show(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute(article);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute(article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String edit(Article editedArticle) {
        Article article = articleService.edit(editedArticle);
        return "redirect:/articles/" + article.getId();
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/";
    }
}
