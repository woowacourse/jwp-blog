package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepository {
    private final List<Category> categories = new ArrayList<>();
    private long latestId = 0;

    public void addCategory(Category category) {
        category.setCategoryId(++latestId);
        categories.add(category);
    }

    public boolean delete(final Category category) {
        return categories.remove(findById(category.getCategoryId()));
    }

    public Category findById(final long categoryId) {
        return categories.stream()
                    .filter(category -> category.getCategoryId() == categoryId)
                    .findFirst().orElseThrow(IllegalArgumentException::new);
    }

    public List<Category> findAll() {
        return new ArrayList<>(categories);
    }
}
