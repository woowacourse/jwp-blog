package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.dto.ArticleDto;
import techcourse.myblog.service.common.ArticleCommonServiceTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

class ArticleWriteServiceTests extends ArticleCommonServiceTests {
    @Test
    void update_test() {
        Long articleId = Long.valueOf(1);
        given(articleRepository.findByIdAndAuthor(articleId, author)).willReturn(Optional.of(article));
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        ArticleDto articleDto = new ArticleDto("title2", "url2", "contents2");
        articleDto.setAuthor(author);
        assertDoesNotThrow(() ->
                articleWriteService.update(articleId, articleDto));

        compareArticle(articleRepository.findById(articleId).get(), articleDto.toArticle());
    }
}