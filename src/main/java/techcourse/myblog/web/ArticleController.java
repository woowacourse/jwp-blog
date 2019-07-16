package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    public String index(Model model) {
        Iterable<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    // TODO : /articles 중복 제거
    @GetMapping("/articles/new")
    public String writeNewArticle() {

        return "article-edit";
    }

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(Article article, Model model) {
        articleRepository.save(article);
        model.addAttribute("article", article);
        System.out.println(articleRepository.findAll());
        return "article";
    }

    @GetMapping("/articles/{id}")
    public String fetchArticle(@PathVariable long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticle(@PathVariable long id, Model model) {
        Article article = articleRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    public String saveEditedArticle(@PathVariable long id, Article editedArticle, Model model) {
        articleRepository.update(editedArticle);
        model.addAttribute("article", editedArticle);
        return "article";
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleRepository.deleteById(id);
        return "redirect:/";
    }
}
