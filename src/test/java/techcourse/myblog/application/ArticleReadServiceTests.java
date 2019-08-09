//package techcourse.myblog.application;
//
//import org.junit.jupiter.api.Test;
//import techcourse.myblog.domain.article.Article;
//import techcourse.myblog.application.common.ArticleCommonServiceTests;
//
//import java.util.Arrays;
//import java.util.Optional;
//
//import static org.mockito.BDDMockito.given;
//
//class ArticleReadServiceTests extends ArticleCommonServiceTests {
//
//    @Test
//    void findAll_test() {
//        given(articleRepository.findAll()).willReturn(Arrays.asList(article));
//
//        articleReadService.findAll().forEach(foundArticle -> compareArticle(foundArticle, article));
//    }
//
//    @Test
//    void findById_test() {
//        Long articleId = 1L;
//        given(articleRepository.findById(articleId)).willReturn(Optional.of(article));
//
//        Article foundArticle = articleReadService.findById(articleId);
//        compareArticle(foundArticle, article);
//    }
//}