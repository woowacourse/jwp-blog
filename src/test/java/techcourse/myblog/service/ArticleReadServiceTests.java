package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.service.common.ArticleCommonServiceTests;

import java.util.Arrays;

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
        Article foundArticle = articleReadService.findById(articleId);
        compareArticle(foundArticle, article);
    }
}