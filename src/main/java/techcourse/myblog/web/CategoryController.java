package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.service.CategoryService;

@Controller
public class CategoryController {
    private CategoryService categoryService;

    @Autowired
    public void CategryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public String createCategory() {
        return "category-edit";
    }

    @PostMapping("/category")
    public String addCategory(String category) {
        categoryService.addCategory(category);
        return "redirect:/";
    }
}
