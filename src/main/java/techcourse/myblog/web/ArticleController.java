package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

import javax.transaction.Transactional;

@Controller
@RequestMapping("/articles")
public class ArticleController {
    private static final String NO_ARTICLE_MESSAGE = "존재하지 않는 게시글 입니다.";
    private static final Logger log = LoggerFactory.getLogger(ArticleController.class);

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/new")
    public String writeArticle() {
        return "article-edit";
    }

    @PostMapping
    public String createArticle(ArticleDto articleDto, Model model) {
        return "redirect:/articles/" + articleService.create(articleDto);
    }

    @GetMapping("/{id}")
    public String showArticle(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("article", articleService.find(id));
            return "article";
        } catch (IllegalArgumentException e) {
            log.error(NO_ARTICLE_MESSAGE);
            return "/";
        }
    }

    @GetMapping("/{id}/edit")
    public String updateArticle(@PathVariable Long id, Model model) {
        try {
            model.addAttribute("article", articleService.find(id));
            return "article-edit";
        } catch (IllegalArgumentException e) {
            log.error(NO_ARTICLE_MESSAGE);
            return "/";
        }
    }

    @Transactional
    @PutMapping("/{id}")
    public String showUpdatedArticle(@PathVariable Long id, ArticleDto updatedArticle, Model model) {
        try {
            model.addAttribute("article", articleService.update(id, updatedArticle));
            return "redirect:/articles/" + id;
        } catch (IllegalArgumentException e) {
            log.error(NO_ARTICLE_MESSAGE);
            return "/";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return "redirect:/";
    }
}
