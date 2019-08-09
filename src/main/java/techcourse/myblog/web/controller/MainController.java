package techcourse.myblog.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.dto.ArticleResponseDto;
import techcourse.myblog.service.ArticleGenericService;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final ArticleGenericService articleGenericService;

    public MainController(ArticleGenericService articleGenericService) {
        this.articleGenericService = articleGenericService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(sort = {"id"}, size = 3, direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("Pageable : {}", pageable);
        model.addAttribute("articles", articleGenericService.findAllPage(pageable, ArticleResponseDto.class));
        return "index";
    }

    @GetMapping("/writing")
    public String getArticleEditForm() {
        return "article-edit";
    }

    @GetMapping("/error")
    public String error() {
        return "error";
    }
}
