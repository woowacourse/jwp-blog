package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CategoryRepositoryTest {

    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        categoryRepository = new CategoryRepository();
    }

    @Test
    void add() {
        categoryRepository.add("주문");
    }

    @Test
    void addExceptionWhenDuplicate() {
        categoryRepository.add("주문");
        assertThrows(IllegalArgumentException.class, () -> categoryRepository.add("주문"));
    }
}
