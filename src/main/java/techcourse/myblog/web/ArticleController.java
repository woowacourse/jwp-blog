package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

@Controller
public class ArticleController {
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/articles/{articleId}")
    public String getArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String getEditArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepository.findById(articleId).orElseThrow(IllegalArgumentException::new);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PostMapping("/articles")
    public String saveArticle(Article article, Model model) {
        model.addAttribute(article);
        articleRepository.save(article);
        return "article";
    }

    @PutMapping("/articles/{articleId}")
    public String getModifiedArticle(@PathVariable long articleId, Article article, Model model) {
        Article originArticle = articleRepository.findById(articleId).orElseThrow(IllegalAccessError::new);
        originArticle.update(article);
        articleRepository.save(originArticle);
        model.addAttribute(originArticle);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleRepository.deleteById(articleId);
        return "redirect:/";
    }
}