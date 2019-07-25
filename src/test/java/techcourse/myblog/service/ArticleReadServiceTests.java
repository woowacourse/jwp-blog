package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.common.ArticleCommonServiceTests;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

class ArticleReadServiceTests extends ArticleCommonServiceTests {

    @Test
    void findAll_test() {
        given(articleRepository.findAll()).willReturn(Arrays.asList(article));

        articleReadService.findAll().forEach(foundArticle ->
                compareArticle(foundArticle, article));
    }

    @Test
    void findById_test() {
        Long articleId = Long.valueOf(1);
        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));

        Optional<Article> articleOpt = articleReadService.findById(articleId);
        assertTrue(articleOpt.isPresent());
        compareArticle(articleOpt.get(), article);
    }
}