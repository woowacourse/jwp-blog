package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.exception.ArticleDtoNotFoundException;
import techcourse.myblog.exception.ArticleNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    private ArticleAssembler assembler;
    private ArticleDto articleDto;
    private int id;

    @Autowired

    private ArticleService service;

    @BeforeEach
    void setUp() {
        assembler = new ArticleAssembler();
        articleDto = new ArticleDto("title", "", "content");
        id = service.save(articleDto);
    }

    // TODO: 2019-07-18 test 한번에 통과하기
    @Test
    void 게시글_생성_확인() {
        assertThat(service.findById(id)).isEqualTo(articleDto);
    }

    @Test
    void 게시글_생성_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(ArticleDtoNotFoundException.class)
                .isThrownBy(() -> service.save(null));
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
        ArticleDto updatedArticleDto = assembler.convertToDto(
                new Article("newTitle", "", "newContent"));
        service.update(1, updatedArticleDto);
        ArticleDto retrievedArticleDto = service.findById(1);
        assertThat(retrievedArticleDto).isEqualTo(updatedArticleDto);
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null일_경우() {
        assertThatExceptionOfType(ArticleDtoNotFoundException.class)
                .isThrownBy(() -> service.update(1, null));
    }

    @Test
    void 게시글_삭제_확인() {
        service.delete(1);
        assertThatExceptionOfType(ArticleNotFoundException.class)
                .isThrownBy(() -> service.findById(id));
    }
}
