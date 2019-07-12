package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.ArticleRequestDto;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {

    @Test
    void update() {
        ArticleRequestDto articleRequestDto = new ArticleRequestDto();
        articleRequestDto.setTitle("a");
        articleRequestDto.setCoverUrl("b");
        articleRequestDto.setContents("c");
        ArticleRequestDto articleRequestDtoToChange = new ArticleRequestDto();
        articleRequestDtoToChange.setTitle("1");
        articleRequestDtoToChange.setCoverUrl("2");
        articleRequestDtoToChange.setContents("3");

        Article article = Article.of(articleRequestDto);
        article.update(articleRequestDtoToChange);
        assertThat(article.getTitle()).isEqualTo("1");
        assertThat(article.getCoverUrl()).isEqualTo("2");
        assertThat(article.getContents()).isEqualTo("3");
    }
}
