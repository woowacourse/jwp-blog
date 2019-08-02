package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.util.ReflectionTestUtils;
import techcourse.myblog.dto.ArticleSaveRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArticleTest {

    @Test
    void isCoverUrl() {
        Article article1 = Article.builder()
                .coverUrl("  ")
                .build();
        assertThat(article1.isCoverUrl()).isFalse();

        Article article2 = Article.builder()
                .coverUrl(null)
                .build();
        assertThat(article2.isCoverUrl()).isFalse();

        Article article3 = Article.builder()
                .coverUrl("coverUrl")
                .build();
        assertThat(article3.isCoverUrl()).isTrue();
    }

    @Test
    void update() {
        Article article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        ArticleSaveRequestDto articleSaveRequestDto = new ArticleSaveRequestDto();
        articleSaveRequestDto.setTitle("newTitle");
        articleSaveRequestDto.setCoverUrl("newCoverUrl");
        articleSaveRequestDto.setContents("newContents");

        article.update(articleSaveRequestDto);

        assertThat(article.getTitle()).isEqualTo("newTitle");
        assertThat(article.getCoverUrl()).isEqualTo("newCoverUrl");
        assertThat(article.getContents()).isEqualTo("newContents");
    }

    @Test
    void isAuthor() {
        User author = User.builder()
                .name("테스트")
                .email("articleAuthor@test.com")
                .password("password1!")
                .build();
        ReflectionTestUtils.setField(author, "id", 1L);

        User anotherAuthor = User.builder()
                .name("테스트")
                .email("articleAnotherAuthor@test.com")
                .password("password1!")
                .build();
        ReflectionTestUtils.setField(anotherAuthor, "id", 2L);

        Article article = Article.builder()
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .author(author)
                .build();

        assertThat(article.isAuthor(author)).isTrue();
        assertThat(article.isAuthor(anotherAuthor)).isFalse();
    }
}