package techcourse.myblog.domain;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class CategoryRepository {
    private List<String> categorys = new ArrayList<>();

    public List<String> findAll() {
        return categorys;
    }

    public void add(String category) {
        if (categorys.contains(category)) {
            throw new IllegalArgumentException("Category is already Exist : " + category);
        }

        categorys.add(category);
    }
}
