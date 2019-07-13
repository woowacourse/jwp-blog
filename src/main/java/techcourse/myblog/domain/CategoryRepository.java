package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepository {
    private List<String> categories = new ArrayList<>();

    public List<String> findAll() {
        return categories;
    }

    public void add(String category) {
        if (categories.contains(category)) {
            throw new IllegalArgumentException("Category is already Exist : " + category);
        }

        categories.add(category);
    }
}
