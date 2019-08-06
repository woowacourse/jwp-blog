package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.NotMatchAuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CommentTest {

    private User user;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("pkch")
                .email("pkch@woowa.com")
                .password("qwerqwer")
                .build();

        comment = Comment.builder()
                .content("hello")
                .user(user)
                .build();

    }

    @Test
    void 댓글을_쓴_유저가_다른_유저인_경우_테스트() {
        User other = User.builder()
                .id(2L)
                .name("park")
                .email("park@woowa.com")
                .password("qwerqwer")
                .build();

        assertThrows(NotMatchAuthenticationException.class, () -> comment.authorizeFor(other));
    }

    @Test
    void 댓글_수정_테스트() {
        String willUpdateContent = "hi";
        Comment updatedComment = comment.update(willUpdateContent, user);

        assertThat(updatedComment.getContent()).isEqualTo(willUpdateContent);
    }
}
