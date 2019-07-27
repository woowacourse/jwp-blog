package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.PageableArticleService;

@Controller
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final PageableArticleService articleService;

    public MainController(PageableArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(sort = {"id"}, size = 3, direction = Sort.Direction.DESC) Pageable pageable) {
        log.debug("Pageable : {}", pageable);
        model.addAttribute("articles", articleService.findAllPage(pageable));
        return "index";
    }

    @GetMapping("/404")
    public String error() {
        return "404";
    }
}
