package techcourse.myblog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import techcourse.myblog.domain.CategoryRepository;

@Service
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public void CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public void addCategory(String category) {
        categoryRepository.add(category);
    }
}
