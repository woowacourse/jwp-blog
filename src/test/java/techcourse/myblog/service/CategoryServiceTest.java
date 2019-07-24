package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.article.ArticleRepository;
import techcourse.myblog.domain.category.Category;
import techcourse.myblog.domain.category.CategoryDto;
import techcourse.myblog.domain.category.CategoryRepository;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.longThat;
import static org.mockito.Mockito.*;

@SpringBootTest
class CategoryServiceTest {
    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ArticleService articleService;

    @Autowired
    private CategoryService categoryService;

    @Test
    void 카테고리_조회() {
        when(categoryRepository.findAll()).thenReturn(new ArrayList<>());
        assertThat(categoryService.readAll()).isEmpty();
    }

    @Test
    void 카테고리_삭제() {
        long categoryId = 1L;
        when(articleService.readByCategoryId(categoryId)).thenReturn(new ArrayList<>());
        categoryService.deleteById(categoryId);
        verify(articleService, times(1)).readByCategoryId(categoryId);
        verify(categoryRepository, times(1)).deleteById(categoryId);
    }

    @Test
    void 카테고리_삭제_실패() {
        long categoryId = 1L;
        ArticleDto articleDto = ArticleDto.builder().build();
        when(articleService.readByCategoryId(categoryId)).thenReturn(Arrays.asList(articleDto));
        categoryService.deleteById(categoryId);
        verify(articleService, times(1)).readByCategoryId(categoryId);
        verify(categoryRepository, times(0)).deleteById(categoryId);
    }

    @Test
    void 카테고리_생성() {
        CategoryDto categoryDto = CategoryDto.builder().build();
        categoryService.create(categoryDto);
        when(categoryRepository.save(categoryDto.toEntity())).thenReturn(categoryDto.toEntity());
        verify(categoryRepository, times(1)).save(categoryDto.toEntity());
    }
}