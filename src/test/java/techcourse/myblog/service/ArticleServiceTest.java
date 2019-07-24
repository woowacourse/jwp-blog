package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.domain.article.ArticleDto;
import techcourse.myblog.domain.article.ArticleRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static reactor.core.publisher.Mono.when;

@RunWith(MockitoJUnitRunner.class)
@TestPropertySource("classpath:application_test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ArticleServiceTest {

    @Autowired
    private ArticleService articleService;

    @MockBean
    private ArticleRepository articleRepository;

    @Test
    void 게시글_생성() {
        long articleId = 3l;
        ArticleDto articleDto = ArticleDto
                .builder()
                .id(articleId)
                .build();

        when((articleRepository.save(articleDto.toEntity()))).thenReturn(articleDto.toEntity());
        assertThat(articleService.createArticle(articleDto)).isEqualTo(articleId);
    }
}