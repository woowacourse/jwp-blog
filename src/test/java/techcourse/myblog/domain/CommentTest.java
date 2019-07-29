package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    Article article;
    User user;

    @BeforeEach
    void setUp() {
        article = new Article("title", "goodPicture.jpg", "contents");
        user = new User("userName", "woowa@gmail.com", "Password123!");
    }

    @Test
    void comment_생성_성공_테스트() {
        String commentContents = "commentContents";
        Comment actual = new Comment(commentContents, user, article);
        assertThat(actual).isNotNull();
    }
}
