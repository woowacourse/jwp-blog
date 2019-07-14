package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.web.dto.ArticleDto;

import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public String createArticle(ArticleDto article) {
        return "redirect:/articles/" + articleRepository.saveArticle(article);
    }

    @GetMapping("/{id}")
    public String goDetailArticle(@PathVariable long id, Model model) {
        model.addAttribute("article",
                articleRepository.getArticleById(id)
                        .orElseThrow(IllegalArgumentException::new));
        return "article";
    }

    @PutMapping("/{id}")
    public String updateArticle(@PathVariable long id, Article updatedArticle) {
        articleRepository.updateArticle(id, updatedArticle);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleRepository.removeArticleById(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editArticle(@PathVariable long id, Model model) {
        model.addAttribute("article",
                articleRepository.getArticleById(id).orElseThrow(IllegalArgumentException::new));
        model.addAttribute("method", PUT);
        return "article-edit";
    }
}
