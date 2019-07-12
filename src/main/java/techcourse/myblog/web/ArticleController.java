package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import static techcourse.myblog.web.MethodType.POST;
import static techcourse.myblog.web.MethodType.PUT;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String goIndex(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String goArticleEdit(Model model) {
        model.addAttribute("method", POST);
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(Article article) {
        articleRepository.saveArticle(article);
        return "redirect:/articles/" + article.getId();
    }

    @GetMapping("/articles/{id}")
    public String goDetailArticle(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable int id, Article updatedArticle) {
        articleRepository.updateArticle(updatedArticle);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleRepository.removeArticleById(id);
        return "redirect:/";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticle(@PathVariable int id, Model model) {
        model.addAttribute("article", articleRepository.getArticleById(id));
        model.addAttribute("method", PUT);
        return "article-edit";
    }
}
