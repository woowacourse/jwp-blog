package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.category.CategoryDto;
import techcourse.myblog.domain.category.CategoryRepository;

import java.util.ArrayList;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ArticleService articleService;

    @InjectMocks
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