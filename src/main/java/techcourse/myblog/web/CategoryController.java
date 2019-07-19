package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.CategoryDto;
import techcourse.myblog.domain.CategoryRepository;
import techcourse.myblog.service.ArticleService;

@Controller
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ArticleService articleService;

    @PostMapping("/categories/add")
    public String addCategories(CategoryDto categoryDto) {
        categoryRepository.save(categoryDto.toCategory());

        return "redirect:/";
    }

    @GetMapping("categories/delete/{categoryId}")
    public String deleteCategories(@PathVariable long categoryId) {
        if (articleService.readByCategoryId(categoryId).isEmpty()) {
            categoryRepository.deleteById(categoryId);
        }

        return "redirect:/";
    }

}
