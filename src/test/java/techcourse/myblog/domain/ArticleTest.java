package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

class ArticleTest {

    @Test
    public void article_update_success() {
        Article article = Article.builder()
                .title("a")
                .coverUrl("b")
                .contents("c")
                .build();
        ArticleDto editedData = ArticleDto.builder()
                .title("aa")
                .coverUrl("bb")
                .contents("cc")
                .build();
        article.update(editedData);
        assertThat(article.getTitle()).isEqualTo(editedData.getTitle());
        assertThat(article.getContents()).isEqualTo(editedData.getContents());
        assertThat(article.getCoverUrl()).isEqualTo(editedData.getCoverUrl());
    }

}
