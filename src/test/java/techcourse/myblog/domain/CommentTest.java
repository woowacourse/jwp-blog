package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class CommentTest {

    Article article;
    User author;

    @BeforeEach
    void setUp() {
        author = new User("userName", "woowa@gmail.com", "Password123!");
        article = new Article(author, "title", "goodPicture.jpg", "contents");
    }

    @Test
    void comment_생성_성공_테스트() {
        String commentContents = "commentContents";
        Comment actual = new Comment(commentContents, author, article);
        assertThat(actual).isNotNull();
    }
}
