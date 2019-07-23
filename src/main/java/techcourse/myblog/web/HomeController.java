package techcourse.myblog.web;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.service.ArticlePagingService;

@Controller
public class HomeController {
    private final ArticlePagingService articlePagingService;

    public HomeController(ArticlePagingService articlePagingService) {
        this.articlePagingService = articlePagingService;
    }

    @GetMapping("/")
    public String index(Model model,
                        @PageableDefault(size = 3, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("articles", articlePagingService.findAll(pageable));
        return "index";
    }
}
