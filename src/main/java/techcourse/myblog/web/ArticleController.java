package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
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
        List<ArticleDto> articleDtos = articleService.findAll();
        model.addAttribute("articleDTOs", articleDtos);
        return "index";
    }

    @GetMapping("/writing")
    public String showWritingPage() {
        return "article-edit";
    }

    @PostMapping("/articles")
    public String createArticle(ArticleDto articleDTO) {
        ArticleDto persistArticle = articleService.save(articleDTO);
        return "redirect:/articles/" + persistArticle.getId();
    }

    @GetMapping("/articles/{id}")
    public String showArticle(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article";
    }

    @PutMapping("/articles/{id}")
    public String updateArticle(@PathVariable int id, ArticleDto articleDTO) {
        articleService.update(id, articleDTO);
        return "redirect:/articles/" + id;
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable int id) {
        articleService.delete(id);
        return "redirect:/";
    }

    @GetMapping("/articles/{id}/edit")
    public String showEditPage(@PathVariable int id, Model model) {
        model.addAttribute("articleDTO", articleService.findById(id));
        return "article-edit";
    }
}
