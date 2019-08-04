package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ArticleTest {
    @Test
    void updateTest() {
        User user = new User("ike", "Password1!", "ike@gmail.com");
        Article origin = new Article("title", "contents", "coverUrl", user);
        Article updated = new Article("updateTitle", "updateContents", "updateCoverUrl", user);
        origin.update(updated);
        assertThat(origin).isEqualTo(updated);
    }
}