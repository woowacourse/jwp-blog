package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.ArticleSaveRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

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

        article.update(articleSaveRequestDto.toArticle());

        assertThat(article.getTitle()).isEqualTo("newTitle");
        assertThat(article.getCoverUrl()).isEqualTo("newCoverUrl");
        assertThat(article.getContents()).isEqualTo("newContents");
    }
}