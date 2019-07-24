package techcourse.myblog.service.article;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.EmptyResultDataAccessException;
import techcourse.myblog.domain.article.Article;
import techcourse.myblog.dto.article.ArticleDto;
import techcourse.myblog.exception.ArticleDtoNotFoundException;
import techcourse.myblog.exception.ArticleNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static techcourse.myblog.service.article.ArticleAssembler.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    private ArticleDto articleDto;
    private Long id;

    @Autowired
    private ArticleService service;

    @BeforeEach
    void setUp() {
        articleDto = new ArticleDto("title", "", "content");
        id = service.save(articleDto);
    }

    @AfterEach
    void tearDown() {
        service.delete(id);
    }

    @Test
    void 게시글_생성_확인() {
        assertThat(service.findById(id)).isEqualTo(articleDto);
    }


    @Test
    void 게시글_조회_확인() {
        ArticleDto retrieveArticleDto = service.findById(id);
        assertThat(retrieveArticleDto).isEqualTo(service.findById(id));
    }

    @Test
    void 모든_게시글_조회_확인() {
        List<ArticleDto> articleDtos = service.findAll();
        assertThat(articleDtos).isEqualTo(Arrays.asList(service.findById(id)));
    }

    @Test
    void 게시글_수정_확인() {
        ArticleDto updatedArticleDto = convertToDto(
                new Article("newTitle", "", "newContent"));
        service.update(id, updatedArticleDto);
        ArticleDto retrievedArticleDto = service.findById(id);
        assertThat(retrievedArticleDto).isEqualTo(updatedArticleDto);
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null일_경우() {
        assertThatExceptionOfType(ArticleDtoNotFoundException.class)
                .isThrownBy(() -> service.update(id, null));
    }

    @Test
    void 게시글_삭제_확인() {
        Long id = service.save(articleDto);
        service.delete(id);
        assertThatExceptionOfType(ArticleNotFoundException.class)
                .isThrownBy(() -> service.findById(id));
    }

    @Test
    void 게시글_삭제_오류확인_없는_게시글_삭제요청할_경우() {
        assertThatExceptionOfType(EmptyResultDataAccessException.class)
                .isThrownBy(() -> service.delete(id + 1));
    }
}
