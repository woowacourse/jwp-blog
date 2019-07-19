package techcourse.myblog.web;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techcourse.myblog.articles.Article;
import techcourse.myblog.articles.ArticleService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class MainController {
    private static final Logger log = LoggerFactory.getLogger(MainController.class);


    private final ArticleService articleService;

    @GetMapping("/")
    public String main(Optional<Integer> page,
                       Optional<Integer> pageSize,
                       Optional<String> order,
                       Optional<String> orderType,
                       Model model) {
        log.debug("page : {}, pageSize : {}, order : {}, orderType : {}", page, pageSize, order, orderType);

        final Integer currentPage = page.orElse(1);
        final Integer size = pageSize.orElse(5);
        final Sort.Direction sorting = Sort.Direction.fromString(order.orElse("DESC"));
        final String sortingType = orderType.orElse("regDate");
        Pageable pageable = PageRequest.of(currentPage, size, sorting, sortingType);

        Page<Article> articles = articleService.findAll(pageable);

        int totalPages = articles.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("order", sorting);
        model.addAttribute("orderType", sortingType);
        model.addAttribute("articles", articles);
        return "index";
    }
}
