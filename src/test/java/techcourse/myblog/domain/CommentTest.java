package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CommentTest {

    @Test
    void 수정_테스트() {
        String contents = "반가워요";
        User user = new User("zino@zino.zino", "hyo.hyo.hyo", "zhiynooh");
        Article article = new Article("title", "coverUrl", "반갑다 나는 효오다", user);
        Comment comment = new Comment("빵가워요", user, article);

        comment.modify(contents, user);

        assertThat(comment.getContents()).isEqualTo(contents);
    }
}