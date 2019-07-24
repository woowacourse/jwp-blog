package techcourse.myblog.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.dto.ArticleRequestDto;
import techcourse.myblog.service.dto.ArticleUpdateDto;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    @Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping("")
    public String createArticle(ArticleRequestDto articleRequestDto) {
        Article newArticle = articleService.save(articleRequestDto.toEntity());
        return "redirect:/articles/" + newArticle.getId();
    }

    @GetMapping("/{id}")
    public String selectArticle(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article";
    }

    @GetMapping("/{id}/edit")
    public String moveArticleEditPage(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article-edit";
    }

    @PutMapping("/{id}")
    public String updateArticle(ArticleUpdateDto articleUpdateDto, Model model) {
        Article updatedArticle = articleService.updateAritlce(articleUpdateDto.toEntity());
        model.addAttribute("article", updatedArticle);
        return "redirect:/articles/" + updatedArticle.getId();
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable long id) {
        articleService.deleteById(id);
        return "redirect:/";
    }
}
