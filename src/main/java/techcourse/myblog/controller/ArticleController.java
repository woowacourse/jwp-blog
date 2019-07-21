package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

import static techcourse.myblog.controller.ArticleController.ARTICLE_MAPPING_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(ARTICLE_MAPPING_URL)
@Slf4j
public class ArticleController {
    public static final String ARTICLE_MAPPING_URL = "/article";
    private final ArticleService articleService;

    @GetMapping("/writing")
    public String createForm() {
        return "article-edit";
    }

    @PostMapping
    public RedirectView save(Article article) {
        log.info(article.getTitle() + " " + article.getCoverUrl() + " " + article.getContents());
        log.warn("A WARN Message");
        log.error("An ERROR Message");
        articleService.save(article);
        return new RedirectView("/article/" + article.getId());
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        log.info("" + id);
        model.addAttribute("article", articleService.findById(id));
        return "article";
    }

    @PutMapping("/{id}")
    public RedirectView update(Article article) {
        log.info("수정할 내용 " + article.getTitle() + " " + article.getCoverUrl() + " " + article.getContents());
        articleService.update(article);
        return new RedirectView("/article/" + article.getId());
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article-edit";
    }

    @DeleteMapping("/{id}")
    public RedirectView delete(@PathVariable long id) {
        log.info(String.valueOf(id));
        articleService.delete(id);
        return new RedirectView("/");
    }
}
