package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
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

    private final long articleId = 3;
    private final long categoryId = 1;
    private ArticleDto articleDto;


    @BeforeEach
    void setUp() {
        articleDto = getArticleDto(articleId, categoryId);
    }

    @Test
    void 게시글_생성() {
        when(articleRepository.save(articleDto.toEntity())).thenReturn(articleDto.toEntity());
        assertThat(articleService.createArticle(articleDto)).isEqualTo(articleId);
    }

    @Test
    void 게시글_조회() {
        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleDto.toEntity()));
        assertThat(articleService.readById(articleId)).isEqualTo(articleDto);
    }

    @Test
    void 게시글_업데이트() {
        ArticleDto inArticleDto = getArticleDto(5, categoryId);

        when(articleRepository.findById(articleId)).thenReturn(Optional.of(articleDto.toEntity()));
        assertThat(articleService.updateByArticle(articleId, inArticleDto)).isEqualTo(articleDto);
    }

    @Test
    void 게시글_ALL_조회() {
        ArticleDto articleDto1 = getArticleDto(1, categoryId);
        ArticleDto articleDto2 = getArticleDto(2, categoryId);

        when(articleRepository.findAll())
                .thenReturn(Arrays.asList(articleDto1.toEntity(), articleDto2.toEntity()));
        assertThat(articleService.readAll())
                .isEqualTo(Arrays.asList(articleDto1, articleDto2));
    }

    @Test
    void 게시글_CategoryId_조회() {
        ArticleDto articleDto1 = getArticleDto(1, categoryId);
        ArticleDto articleDto2 = getArticleDto(1, categoryId);

        when(articleRepository.findAll())
                .thenReturn(Arrays.asList(articleDto1.toEntity(), articleDto2.toEntity()));
        assertThat(articleService.readAll())
                .isEqualTo(Arrays.asList(articleDto1, articleDto2));
    }

    private  ArticleDto getArticleDto(long id, long categoryId) {
        return ArticleDto.builder()
                .id(id)
                .categoryId(categoryId)
                .build();
    }

}