package techcourse.myblog.presentation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.ArticleService;

import static techcourse.myblog.presentation.controller.ArticleController.ARTICLE_MAPPING_URL;

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
    public RedirectView save(ArticleDto articleDto) {
        return new RedirectView("/article/" + articleService.save(articleDto).getId());
    }

    @GetMapping("/{id}")
    public String show(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article";
    }

    @PutMapping("/{id}")
    public RedirectView update(@PathVariable long id, ArticleDto articleDto) {
        log.info("수정할 내용 " + articleDto.getTitle() + " " + articleDto.getCoverUrl() + " " + articleDto.getContents());
        articleService.update(articleDto, id);
        return new RedirectView("/article/" + id);
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable long id, Model model) {
        model.addAttribute("article", articleService.findById(id));
        return "article-edit";
    }

    @DeleteMapping("/{id}")
    public RedirectView delete(@PathVariable long id) {
        articleService.delete(id);
        return new RedirectView("/");
    }
}
