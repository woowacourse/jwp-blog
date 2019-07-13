package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.ArticleDTO;
import techcourse.myblog.service.ArticleService;

import java.util.List;

@Controller
public class ArticleController {
    private ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String showMain(Model model) {
        List<ArticleDTO> articleDTOs = articleService.findAll();
        model.addAttribute("articleDTOs", articleDTOs);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDTO articleDTO) {
        int id = articleService.createArticle(articleDTO);
        return "redirect:/articles/" + id;
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleService.findArticleById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable int id, ArticleDTO articleDTO) {
        articleService.updateArticle(id, articleDTO);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleService.deleteArticle(id);
        return "redirect:/";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleService.findArticleById(id));
        return "article-edit";
    }
}
