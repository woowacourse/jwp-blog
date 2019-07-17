package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.ArticleRepositoryImpl;
import techcourse.myblog.domain.Category;
import techcourse.myblog.domain.CategoryRepository;

@Controller
public class CategoryController {

    @Autowired
    private ArticleRepositoryImpl articleRepositoryImpl;

    @Autowired
    private CategoryRepository categoryRepositoryImpl;

    @PostMapping("/categories/add")
    public String addCategories(Category category) {
        categoryRepositoryImpl.addCategory(category);

        return "redirect:/";
    }

    @GetMapping("categories/delete/{categoryId}")
    public String deleteCategories(@PathVariable long categoryId) {
        Category category = new Category();
        category.setCategoryId(categoryId);

        if (articleRepositoryImpl.findByCategoryId(categoryId).isEmpty()) {
            categoryRepositoryImpl.delete(category);
        }

        return "redirect:/";
    }

}
