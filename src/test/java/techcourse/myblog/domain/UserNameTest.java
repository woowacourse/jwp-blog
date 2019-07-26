package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.domain.user.UserName;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserNameTest {

    @Test
    void 두글자_미만() {
        assertThatThrownBy(() -> new UserName("a")).isInstanceOf(UserException.class);
    }

    @Test
    void 열자_초과() {
        assertThatThrownBy(() -> new UserName("aabbccddeef")).isInstanceOf(UserException.class);
    }

    @Test
    void 특수문자_포함() {
        assertThatThrownBy(() -> new UserName("aabbccdd@")).isInstanceOf(UserException.class);
    }

    @Test
    void 정상_케이스() {
        assertDoesNotThrow(() -> new UserName("abcd"));
    }
}