package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.domain.ArticleRepository;
import techcourse.myblog.exception.ArticleDtoNotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class ArticleServiceTest {
    private ArticleService service;
    private ArticleAssembler assembler;
    private ArticleDto articleDto;
    private ArticleDto persistArticleDto;

    @BeforeEach
    void setUp() {
        service = new ArticleService(new ArticleRepository());
        assembler = new ArticleAssembler();
        articleDto = assembler.convertToDto(new Article(1, "title", "", "content"));
        persistArticleDto = service.save(articleDto);
    }

    @Test
    void 게시글_생성_확인() {
        assertThat(persistArticleDto).isEqualTo(articleDto);
    }

    @Test
    void 게시글_생성_오류확인_게시글이_null인_경우() {
        assertThatExceptionOfType(ArticleDtoNotFoundException.class)
                .isThrownBy(() -> service.save(null));
    }

    @Test
    void 게시글_조회_확인() {
        ArticleDto retrieveArticleDto = service.findById(persistArticleDto.getId());
        assertThat(retrieveArticleDto).isEqualTo(persistArticleDto);
    }

    @Test
    void 모든_게시글_조회_확인() {
        List<ArticleDto> articleDtos = service.findAll();
        assertThat(articleDtos).isEqualTo(Arrays.asList(persistArticleDto));
    }

    @Test
    void 게시글_수정_확인() {
        ArticleDto updatedArticleDto = assembler.convertToDto(
                new Article(persistArticleDto.getId(), "newTitle", "", "newContent"));
        service.update(persistArticleDto.getId(), updatedArticleDto);
        assertThat(service.findById(persistArticleDto.getId())).isEqualTo(updatedArticleDto);
    }

    @Test
    void 게시글_수정_오류확인_게시글이_null일_경우() {
        assertThatExceptionOfType(ArticleDtoNotFoundException.class)
                .isThrownBy(() -> service.update(1, null));
    }

    @Test
    void 게시글_삭제_확인() {
        service.delete(persistArticleDto.getId());
        List<ArticleDto> articleDtos = service.findAll();
        assertThat(articleDtos.contains(persistArticleDto)).isFalse();
    }
}
