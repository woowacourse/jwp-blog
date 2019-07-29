package techcourse.myblog.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import techcourse.myblog.service.dto.ArticleDto;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    @Autowired
    ArticleService articleService;

    private Long articleId;

    @BeforeEach
    void setUp() {
        ArticleDto articleDto = articleService.save(
                1L, new ArticleDto(5L, 1L, "title", "coverUrl", "contents"));
        articleId = articleDto.getId();
    }

    @Test
    void Article_userId와_수정하려는_User의_Id가_다르면_수정_실패() {
        ArticleDto updateArticleDto =
                new ArticleDto(articleId, 1L, "title1", "coverUrl1", "contents1");

        articleService.update(articleId, 2L, updateArticleDto);
        ArticleDto updateFailArticle = articleService.findArticleDtoById(articleId);

        assertThat(updateFailArticle.getTitle()).isEqualTo("title");
        assertThat(updateFailArticle.getCoverUrl()).isEqualTo("coverUrl");
        assertThat(updateFailArticle.getContents()).isEqualTo("contents");
    }

    @Test
    void Article_userId와_삭제하려는_User의_Id가_다르면_삭제_실패() {
        articleService.delete(articleId, 2L);
        ArticleDto deleteFailArticle = articleService.findArticleDtoById(articleId);

        assertThat(deleteFailArticle.getTitle()).isEqualTo("title");
        assertThat(deleteFailArticle.getCoverUrl()).isEqualTo("coverUrl");
        assertThat(deleteFailArticle.getContents()).isEqualTo("contents");
    }
}
