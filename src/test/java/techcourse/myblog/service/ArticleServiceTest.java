package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.ArticleService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    private ArticleDto.Response articleDto;
    private long userId = 1L;

    @BeforeEach
    void setUp() {
        ArticleDto.Request articleRequest = ArticleDto.Request.builder()
                .contents("contents")
                .coverUrl("coverUrl")
                .title("title")
                .build();

        Long id = articleService.save(userId, articleRequest);
        articleDto = articleService.getOne(id);
    }

    @Test
    void findById() {
        assertThat(articleService.getOne(articleDto.getId())).isNotNull();
    }

    @Test
    @DisplayName("getOne 없는 id로 검색했을 경우 예외처리")
    void findById_예외처리() {
        assertThrows(IllegalArgumentException.class, () -> articleService.getOne(Long.MAX_VALUE));
    }

    @Test
    void edit() {
        // given
        final ArticleDto.Request editedArticle = ArticleDto.Request.builder()
                .id(articleDto.getId())
                .contents("1")
                .coverUrl("2")
                .title("3")
                .build();

        // when
        Long id = articleService.edit(userId, editedArticle);
        ArticleDto.Response expected = articleService.getOne(id);

        // then
        assertThat(editedArticle.getContents()).isEqualTo(expected.getContents());
        assertThat(editedArticle.getCoverUrl()).isEqualTo(expected.getCoverUrl());
        assertThat(editedArticle.getTitle()).isEqualTo(expected.getTitle());
    }
}