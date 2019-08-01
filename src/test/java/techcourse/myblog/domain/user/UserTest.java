package techcourse.myblog.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void 생성자_오류확인_이메일이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new User(null, "done", "12345678"));
    }

    @Test
    void 생성자_오류확인_이름이_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new User("done@gmail.com", null, "12345678"));
    }

    @Test
    void 생성자_오류확인_비밀번호가_null일_경우() {
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> new User("done@gmail.com", "done", null));
    }

    @Test
    void 회원정보_수정확인_이름변경() {
        user.update("dowon");
        assertThat(user).isEqualTo(new User("done@gmail.com", "dowon", "12345678"));
    }
}
