package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleAssemblerTest {
    private ArticleAssembler assembler;
    private ArticleDto articleDto;
    private Article article;

    @BeforeEach
    void setUp() {
        assembler = new ArticleAssembler();
        articleDto = new ArticleDto("title", "", "contents");
        article = new Article("title", "", "contents");
    }

    @Test
    void dto를_entity로_변환하는지_확인() {
        assertThat(assembler.convertToEntity(articleDto)).isEqualTo(article);
    }

    @Test
    void entity를_dto로_변환하는지_확인() {
        assertThat(assembler.convertToDto(article)).isEqualTo(articleDto);
    }
}
