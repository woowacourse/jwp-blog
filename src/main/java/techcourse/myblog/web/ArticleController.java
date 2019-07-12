package techcourse.myblog.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

import java.util.List;

@Controller
public class ArticleController {
    private final ArticleRepository articleRepository;

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/")
    private String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/articles/{id}")
    private String show(@PathVariable int id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("id", id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("coverUrl", article.getCoverUrl());
        model.addAttribute("contents", article.getContents());
        return "article";
    }

    @GetMapping("/writing")
    private String createForm() {
        return "article-edit";
    }

    @PostMapping("/articles")
    private String create(@ModelAttribute Article article, Model model) {
        articleRepository.addBlog(article);
        int id = articleRepository.findAll().size() - 1;

        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}/edit")
    private String updateForm(@PathVariable int id, Model model) {
        Article article = articleRepository.find(id);
        model.addAttribute("id", id);
        model.addAttribute("title", article.getTitle());
        model.addAttribute("coverUrl", article.getCoverUrl());
        model.addAttribute("contents", article.getContents());
        return "article-edit";
    }

    @PutMapping("/articles/{id}")
    private String update(@ModelAttribute Article article, @PathVariable int id, Model model) {
        Article target = articleRepository.find(id);
        target.setTitle(article.getTitle());
        target.setContents(article.getContents());
        target.setCoverUrl(article.getCoverUrl());
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    private String delete(@PathVariable int id) {
        articleRepository.delete(id);
        return "redirect:/";
    }
}