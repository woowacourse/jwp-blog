package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.category.CategoryDto;
import techcourse.myblog.service.CategoryService;

@Controller
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/categories/add")
    public String addCategories(CategoryDto categoryDto) {
        categoryService.create(categoryDto);
        return "redirect:/";
    }

    @GetMapping("/categories/delete/{categoryId}")
    public String deleteCategories(@PathVariable long categoryId) {
        categoryService.deleteById(categoryId);

        return "redirect:/";
    }

}
