package techcourse.myblog.application;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.common.ArticleCommonServiceTests;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;
import static techcourse.myblog.utils.ArticleTestObjects.UPDATE_ARTICLE_DTO;

class ArticleWriteServiceTests extends ArticleCommonServiceTests {
    @Test
    void update_test() {
        Long articleId = 1L;
        given(articleRepository.findByIdAndAuthor(articleId, author)).willReturn(Optional.of(article));
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        assertDoesNotThrow(() -> articleWriteService.update(articleId, UPDATE_ARTICLE_DTO.toArticle(author)));

        compareArticle(articleRepository.findById(articleId).get(), UPDATE_ARTICLE_DTO.toArticle(author));
    }
}