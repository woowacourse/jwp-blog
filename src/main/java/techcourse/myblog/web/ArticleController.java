package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static techcourse.myblog.domain.MethodType.PUT;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private ArticleRepository articleRepository;

    @Autowired
    public void setArticleRepository(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public String createArticle(Article article) {
        articleRepository.saveArticle(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/{id}")
    public String goDetailArticle(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        return "article";
    }

    @PutMapping("/{id}")
    public String updateArticle(@PathVariable int id, Article updatedArticle) {
        articleRepository.updateArticle(updatedArticle);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.removeArticleById(id);
        return "redirect:/";
    }

    @GetMapping("/{id}/edit")
    public String editArticle(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        model.addAttribute("method", PUT);
        return "article-edit";
    }
}
