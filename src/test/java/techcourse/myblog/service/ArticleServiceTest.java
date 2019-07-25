package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.article.ArticleRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ArticleServiceTest {
    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService articleService;

    @Test
    void 게시글_생성() {
        long articleId = 3l;
        ArticleDto articleDto = ArticleDto.builder()
                .id(articleId)
                .build();

        when(articleRepository.save(articleDto.toEntity())).thenReturn(articleDto.toEntity());
        assertThat(articleService.createArticle(articleDto)).isEqualTo(articleId);
    }

    @Test
    void 게시글_조회() {
        long articleId = 3l;
        ArticleDto articleDto = ArticleDto.builder()
                .id(articleId)
                .build();

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleDto.toEntity()));
        assertThat(articleService.readById(articleId)).isEqualTo(articleDto);
    }

    @Test
    void 게시글_업데이트() {
        long articleId = 3l;
        ArticleDto inArticleDto = ArticleDto.builder()
                .id(5l)
                .build();

        ArticleDto outArticleDto = ArticleDto.builder()
                .id(articleId)
                .build();

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(outArticleDto.toEntity()));
        assertThat(articleService.updateByArticle(articleId, inArticleDto)).isEqualTo(outArticleDto);
    }

    @Test
    void 게시글_ALL_조회() {
        ArticleDto articleDto1 = ArticleDto.builder()
                .id(1l)
                .categoryId(1)
                .build();

        ArticleDto articleDto2 = ArticleDto.builder()
                .id(2l)
                .categoryId(2)
                .build();

        when(articleRepository.findAll())
                .thenReturn(Arrays.asList(articleDto1.toEntity(), articleDto2.toEntity()));
        assertThat(articleService.readAll())
                .isEqualTo(Arrays.asList(articleDto1, articleDto2));
    }

    @Test
    void 게시글_CategoryId_조회() {
        ArticleDto articleDto1 = ArticleDto.builder()
                .id(1l)
                .categoryId(1)
                .build();

        ArticleDto articleDto2 = ArticleDto.builder()
                .id(2l)
                .categoryId(1)
                .build();

        when(articleRepository.findAll())
                .thenReturn(Arrays.asList(articleDto1.toEntity(), articleDto2.toEntity()));
        assertThat(articleService.readAll())
                .isEqualTo(Arrays.asList(articleDto1, articleDto2));
    }
}