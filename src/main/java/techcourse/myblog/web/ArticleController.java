package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.dto.ArticleDto;

import java.util.List;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/")
    public String index(Model model) {
        List<Article> articles = articleRepository.findAll();
        model.addAttribute("articles", articles);
        return "index";
    }

    @GetMapping("/writing")
    public String renderCreatePage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto.Create articleDto) {
        Article article = new Article(articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());
        long articleId = articleRepository.save(article);
        return "redirect:/articles/" + articleId;
    }

    @GetMapping("/articles/{articleId}")
    public String readArticle(@PathVariable long articleId, Model model) {
        Article article = articleRepository.find(articleId);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String renderUpdatePage(@PathVariable long articleId, Model model) {
        Article article = articleRepository.find(articleId);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String updateArticle(@PathVariable long articleId, ArticleDto.Update articleDto) {
        Article article = new Article(articleId, articleDto.getTitle(), articleDto.getCoverUrl(), articleDto.getContents());
        articleRepository.update(article);
        return "redirect:/articles/" + articleId;
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable long articleId) {
        articleRepository.delete(articleId);
        return "redirect:/";
    }
}
