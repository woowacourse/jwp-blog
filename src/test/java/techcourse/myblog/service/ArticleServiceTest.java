package techcourse.myblog.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import techcourse.myblog.AbstractTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ArticleServiceTest extends AbstractTest {
    private static long articleId = 1;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ModelMapper modelMapper;

    private Article article;

    @BeforeEach
    void setUp() {
        article = Article.builder()
                .id(articleId)
                .title("title")
                .coverUrl("coverUrl")
                .contents("contents")
                .build();

        articleService.save(modelMapper.map(article, ArticleDto.Create.class));
    }

    @Test
    void 게시글_전체_조회_테스트() {
        assertThat(articleService.findAll()).isEqualTo(
                Arrays.asList(modelMapper.map(article, ArticleDto.Response.class)));
    }

    @Test
    void 게시글_단건_조회_테스트() {
        assertThat(articleService.findById(articleId)).isEqualTo(modelMapper.map(article, ArticleDto.Response.class));
    }

    @Test
    void 게시글_수정_테스트() {
        Article updatedUser = Article.builder()
                .id(articleId)
                .title("updatedTitle")
                .coverUrl("updatedCoverUrl")
                .contents("updatedContents")
                .build();
        long updatedArticleId = articleService.update(articleId, modelMapper.map(updatedUser, ArticleDto.Update.class));
        assertThat(articleService.findById(updatedArticleId))
                .isEqualTo(modelMapper.map(updatedUser, ArticleDto.Response.class));
    }

    @AfterEach
    void tearDown() {
        articleService.deleteById(articleId++);
    }
}