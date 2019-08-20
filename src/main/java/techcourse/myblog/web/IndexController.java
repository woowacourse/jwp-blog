package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import techcourse.myblog.service.ArticleService;

@Controller
public class IndexController {
    private static final Logger log = LoggerFactory.getLogger(IndexController.class);
    private static final int DEFAULT_ARTICLES_PER_PAGE = 10;
    private static final Sort SORT_ARTICLE_BY_ID_DSC = Sort.by("id").descending();

    private final ArticleService articleService;

    private int articlesPerPage = DEFAULT_ARTICLES_PER_PAGE;
    private Sort articleSort = SORT_ARTICLE_BY_ID_DSC;

    public void setArticlesPerPage(int articlesPerPage) {
        this.articlesPerPage = articlesPerPage;
    }

    public void setArticleSort(Sort articleSort) {
        this.articleSort = articleSort;
    }

    public IndexController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "1") int pageStartFrom1, Model model) {
        log.debug("begin");

        model.addAttribute("articles", articleService.findAll(indexPageable(pageStartFrom1)));
        return "index";
    }

    private Pageable indexPageable(int pageStartFrom1) {
        return PageRequest.of(pageStartFrom1 - 1, articlesPerPage, articleSort);
    }
}
