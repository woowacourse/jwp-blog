package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommentTest {

    @Test
    void 생성_테스트() {
        Comment comment = new Comment("contents");
        System.out.println(comment.getLocalDate());
        System.out.println(comment.getLocalTime());
    }
}