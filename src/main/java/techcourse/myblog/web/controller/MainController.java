package techcourse.myblog.web.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.ArticleService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final ArticleService articleService;

    @GetMapping("/")
    public String main(@PageableDefault(size = 5, sort = "regDate", direction = Sort.Direction.DESC) Pageable pageable,
                       Model model) {

        log.debug("page : {}, pageSize : {}, sort : {}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());

        Page<Article> articles = articleService.findAll(pageable);

        int totalPages = articles.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        String sortProperty = pageable.getSort().stream()
                .map(Sort.Order::getProperty)
                .findFirst().get();

        Sort.Direction sort = pageable.getSort().stream()
                .map(Sort.Order::getDirection)
                .findFirst().get();

        model.addAttribute("sortProperty", sortProperty);
        model.addAttribute("sort", sort);
        model.addAttribute("articles", articles);
        return "index";
    }
}
