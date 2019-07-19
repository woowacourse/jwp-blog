package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.Category;
import techcourse.myblog.domain.CategoryDto;
import techcourse.myblog.domain.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CategoryRepository categoryRepository;

    public List<CategoryDto> readAll() {
        List<CategoryDto> categories = new ArrayList<>();
        for (Category category : categoryRepository.findAll()) {
            categories.add(CategoryDto.from(category));
        }
        return categories;
    }

    public void deleteById(long categoryId) {
        if (articleService.readByCategoryId(categoryId).isEmpty()) {
            categoryRepository.deleteById(categoryId);
        }
    }

    public void create(CategoryDto categoryDto) {
        categoryRepository.save(categoryDto.toCategory());
    }
}
