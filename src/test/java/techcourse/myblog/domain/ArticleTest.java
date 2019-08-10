package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    private static final String NAME = "이름";
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "@Password1234";

    private static final String TITLE = "달";
    private static final String CONTENTS = "\uDF19";
    private static final String COVER_URL = "";

    @Test
    void updateTest() {
        String changed = "changed";
        User user = new User(NAME, PASSWORD, EMAIL);
        Article originArticle = new Article(TITLE, CONTENTS, COVER_URL, user);
        Article updatedArticle = new Article(changed + TITLE, changed + CONTENTS, changed + COVER_URL, user);

        originArticle.update(updatedArticle, user);

        assertThat(originArticle).isEqualTo(updatedArticle);
    }
}