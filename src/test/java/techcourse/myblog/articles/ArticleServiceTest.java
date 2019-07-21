package techcourse.myblog.articles;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleServiceTest {

    @Autowired
    ArticleService articleService;

    private Article article;

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .contents("contents")
                .coverUrl("coverUrl")
                .title("title")
                .build();

        article = articleService.save(article);
    }

    @Test
    void findById() {
        assertThat(articleService.findById(article.getId())).isNotNull();
    }

    @Test
    @DisplayName("findById 없는 id로 검색했을 경우 예외처리")
    void findById_예외처리() {
        assertThrows(IllegalArgumentException.class, ()-> articleService.findById(100L));
    }

    @Test
    void edit() {
        // given
        final Article editedArticle = Article.builder()
                .contents("1")
                .coverUrl("2")
                .title("3")
                .build();

        article.update(editedArticle);

        // when
        Article expected = articleService.edit(article);

        // then
        assertThat(editedArticle.getContents()).isEqualTo(expected.getContents());
        assertThat(editedArticle.getCoverUrl()).isEqualTo(expected.getCoverUrl());
        assertThat(editedArticle.getTitle()).isEqualTo(expected.getTitle());
    }
}