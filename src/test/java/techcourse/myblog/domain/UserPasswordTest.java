package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.UserException;
import techcourse.myblog.domain.user.UserPassword;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserPasswordTest {

    @Test
    void 대문자_미포함() {
        assertThatThrownBy(() -> UserPassword.of("a!1bcdefg")).isInstanceOf(UserException.class);
    }

    @Test
    void 소문자_미포함() {
        assertThatThrownBy(() -> UserPassword.of("A!1BCDEFG")).isInstanceOf(UserException.class);
    }

    @Test
    void 특수문자_미포함() {
        assertThatThrownBy(() -> UserPassword.of("a!1bcdefg")).isInstanceOf(UserException.class);
    }

    @Test
    void 숫자_미포함() {
        assertThatThrownBy(() -> UserPassword.of("A!bcdefg")).isInstanceOf(UserException.class);
    }

    @Test
    void 여덟자_미만() {
        assertThatThrownBy(() -> UserPassword.of("A1!bcde")).isInstanceOf(UserException.class);
    }

    @Test
    void 정상_케이스() {
        assertDoesNotThrow(() -> UserPassword.of("A!1bcdefg"));
    }
}