package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleSaveDto;
import techcourse.myblog.service.ArticleService;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/new")
    public String writeNewArticle() {
        return "article-edit";
    }

    @GetMapping("/writing")
    public String writeArticleForm() {
        return "article-edit";
    }

    @PostMapping("")
    public String saveArticle(ArticleSaveDto articleSaveDto, Model model) {
        Article article = articleService.save(articleSaveDto.toEntity());
        model.addAttribute("article", article);
        System.out.println(articleService.findAllArticles());
        return "article";
    }

    @GetMapping("/{id}")
    public String fetchArticle(@PathVariable long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String editArticle(@PathVariable long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String saveEditedArticle(@PathVariable long id, ArticleSaveDto articleSaveDto, Model model) {
        articleService.update(articleSaveDto.toEntity(), id);
        model.addAttribute("article", articleSaveDto.toEntity());
        return "article";
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleService.deleteById(id);
        return "redirect:/";
    }
}
