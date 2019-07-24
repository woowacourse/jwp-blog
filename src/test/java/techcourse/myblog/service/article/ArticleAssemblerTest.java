package techcourse.myblog.service.article;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.article.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.service.article.ArticleAssembler.*;

public class ArticleAssemblerTest {
    private ArticleDto articleDto;
    private Article article;

    @BeforeEach
    void setUp() {
        articleDto = new ArticleDto("title", "", "contents");
        article = new Article("title", "", "contents");
    }

    @Test
    void dto를_entity로_변환하는지_확인() {
        assertThat(convertToEntity(articleDto)).isEqualTo(article);
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        assertThat(convertToDto(article)).isEqualTo(articleDto);
    }
}
