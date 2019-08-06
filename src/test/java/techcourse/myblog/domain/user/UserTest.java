package techcourse.myblog.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.UserHasNotAuthorityException;
import techcourse.myblog.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("done@gmail.com", "done", "12345678");
    }

    @Test
    void 생성자_확인() {
        assertThat(user)
                .isEqualTo(new User("done@gmail.com", "done", "12345678"));
    }

    @Test
    void 회원정보_수정확인_이름변경() {
        user.update(new User("done@gmail.com", "john", null));
        assertThat(user).isEqualTo(new User("done@gmail.com", "dowon", "12345678"));
    }

    @Test
    void 회원정보_수정_오류확인_다른_회원이_정보를_수정할_경우() {
        assertThatExceptionOfType(UserHasNotAuthorityException.class)
                .isThrownBy(() -> user.update(new User("john123@example.com", "john", null)));
    }

    @Test
    void 동일회원인지_비교_확인() {
        assertThat(user.match(new User("done@gmail.com", "done", "12345678"))).isTrue();
    }

    @Test
    void 동일회원인지_비교_확인_다른_회원일_경우() {
        assertThat(user.match(new User("john123@example.com", "john", "p@ssW0rd"))).isFalse();
    }
}
