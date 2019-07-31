package techcourse.myblog.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.category.CategoryDto;
import techcourse.myblog.service.ArticleService;
import techcourse.myblog.service.CategoryService;

import java.util.List;

@Controller
public class RootController {
    private static final Logger log = LoggerFactory.getLogger(RootController.class);

    private final ArticleService articleService;
    private final CategoryService categoryService;

    @Autowired
    public RootController(ArticleService articleService, CategoryService categoryService) {
        this.articleService = articleService;
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public String index(Model model) {
        log.debug("index");
        List<ArticleDto> articles = articleService.readAll();
        List<CategoryDto> categories = categoryService.readAll();

        setModel(model, articles, categories);

        return "index";
    }

    @GetMapping("/{categoryId}")
    public String index(@PathVariable final long categoryId, Model model) {
        List<ArticleDto> articles = articleService.readByCategoryId(categoryId);
        List<CategoryDto> categories = categoryService.readAll();

        setModel(model, articles, categories);

        return "index";
    }

    private void setModel(Model model, List<ArticleDto> articles, List<CategoryDto> categories) {
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);
    }

}
