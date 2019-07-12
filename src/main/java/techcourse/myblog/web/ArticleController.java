package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.ArticleDto;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.ArticleRepository;

@Controller
public class ArticleController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String createArticleForm() {
        return "article-edit";
    }

    @PostMapping("/write")
    public String createArticle(ArticleDto articleDto) {
        int id = articleRepository.nextId();
        Article article = new Article(
                id,
                articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents());
        articleRepository.insert(article);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("articles", articleRepository.findAll());
        return "index";
    }

    @GetMapping("/articles/{articleId}")
    public String showArticle(@PathVariable Integer articleId, Model model) {
        model.addAttribute("article", articleRepository.find(articleId));
        return "article";
    }

    @GetMapping("/articles/{articleId}/edit")
    public String editArticleForm(@PathVariable Integer articleId, Model model) {
        model.addAttribute("article", articleRepository.find(articleId));
        return "article-edit";
    }

    @PutMapping("/articles/{articleId}")
    public String editArticle(@PathVariable Integer articleId, ArticleDto articleDto, Model model) {
        Article article = new Article(
                articleId,
                articleDto.getTitle(),
                articleDto.getCoverUrl(),
                articleDto.getContents());
        articleRepository.update(article);
        model.addAttribute("article", article);
        return "article";
    }

    @DeleteMapping("/articles/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        articleRepository.remove(articleId);
        return "redirect:/";
    }
}
