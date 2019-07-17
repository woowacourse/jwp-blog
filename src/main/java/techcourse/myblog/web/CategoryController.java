package techcourse.myblog.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import techcourse.myblog.domain.*;

@Controller
public class CategoryController {
    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @PostMapping("/categories/add")
    public String addCategories(CategoryDto categoryDto) {
        categoryRepository.save(categoryDto.toCategory());

        return "redirect:/";
    }

    @GetMapping("categories/delete/{categoryId}")
    public String deleteCategories(@PathVariable long categoryId) {
        if (articleRepository.findByCategoryId(categoryId).isEmpty()) {
            categoryRepository.deleteById(categoryId);
        }

        return "redirect:/";
    }

}
