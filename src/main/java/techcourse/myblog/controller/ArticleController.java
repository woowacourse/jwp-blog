package techcourse.myblog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.domain.Article;
import techcourse.myblog.repository.ArticleRepository;

import static techcourse.myblog.controller.ArticleController.ARTICLE_MAPPING_URL;

@Controller
@RequiredArgsConstructor
@RequestMapping(ARTICLE_MAPPING_URL)
@Slf4j
public class ArticleController {
    public static final String ARTICLE_MAPPING_URL = "/article";
    private final ArticleRepository articleRepository;

    @GetMapping("/writing")
    public String createForm() {
        return "article-edit";
    }

    @PostMapping
    public RedirectView save(Article article) {
        log.debug(article.toString());
        articleRepository.save(article);
        return new RedirectView("/article/" + article.getId());
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        log.debug("show : " + id);
        model.addAttribute("article", articleRepository.findById(id).get());
        return "article";
    }

    @PutMapping("/{id}")
    public RedirectView update(Article article) {
        log.debug("update - Controller : " + article.toString());
        articleRepository.save(article);
        return new RedirectView("/article/" + article.getId());
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable long id, Model model) {
        model.addAttribute("article", articleRepository.findById(id).get());
        return "article-edit";
    }

    @DeleteMapping("/{id}")
    public RedirectView delete(@PathVariable long id) {
        log.debug(String.valueOf(id));
        articleRepository.deleteById(id);
        return new RedirectView("/");
    }
}
