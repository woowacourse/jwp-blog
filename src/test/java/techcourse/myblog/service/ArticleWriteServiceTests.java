package techcourse.myblog.service;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.Article;
import techcourse.myblog.domain.User;
import techcourse.myblog.service.common.ArticleCommonServiceTests;
import techcourse.myblog.service.exception.MismatchAuthorException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ArticleWriteServiceTests extends ArticleCommonServiceTests {
    @Test
    void update_success_test() {
        Article updateArticle = new Article("newTitle", "newCoverUrl", "newContents", author);
        assertDoesNotThrow(() ->
                articleWriteService.update(articleId, updateArticle));

        compareArticle(articleRepository.findById(articleId).get(), article);
    }

    @Test
    void update_fail_test() {
        User other = new User("other", "other@mail.com", "Passw0rd!");
        Article updateArticle = new Article("newTitle", "newCoverUrl", "newContents", other);

        assertThrows(MismatchAuthorException.class, () ->
                articleWriteService.update(articleId, updateArticle));
    }
}