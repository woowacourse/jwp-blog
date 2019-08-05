package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.dto.ArticleDto;
import techcourse.myblog.application.service.exception.NotMatchArticleAuthorException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ArticleTest {
    private static final Long USER_ID = 1L;
    private static final String EMAIL = "aa@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "password";
    private static final Long ARTICLE_ID = 2L;
    private static final String TITLE = "ThisIsTitle";
    private static final String COVER_URL = "ThisIsCoverUrl";
    private static final String CONTENTS = "ThisIsContents";

    @Test
    void 수정_테스트() {
        User user = new User(USER_ID, EMAIL, NAME, PASSWORD);
        Article article = new Article(ARTICLE_ID, TITLE, COVER_URL, CONTENTS, user);

        ArticleDto articleDto = new ArticleDto(null, TITLE + "a", COVER_URL + "b", CONTENTS + "c", null);
        article.modify(articleDto, user);

        assertThat(article.getTitle()).isEqualTo(TITLE + "a");
        assertThat(article.getCoverUrl()).isEqualTo(COVER_URL + "b");
        assertThat(article.getContents()).isEqualTo(CONTENTS + "c");
    }

    @Test
    void 작성자와_다른_유저의_수정_에러_테스트() {
        User user = new User(USER_ID, EMAIL, NAME, PASSWORD);
        Article article = new Article(ARTICLE_ID, TITLE, COVER_URL, CONTENTS, user);
        User anotherUser = new User(USER_ID + 1L, EMAIL + "a", NAME + "a", PASSWORD + "a");

        ArticleDto articleDto = new ArticleDto(null, TITLE + "a", COVER_URL + "b", CONTENTS + "c", null);
        assertThrows(NotMatchArticleAuthorException.class, () -> article.modify(articleDto, anotherUser));
    }
}