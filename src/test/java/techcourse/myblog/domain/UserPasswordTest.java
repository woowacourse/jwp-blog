package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserPasswordTest {

    @Test
    void 대문자_미포함() {
        assertThatThrownBy(() -> new UserPassword("a!1bcdefg")).isInstanceOf(UserException.class);
    }

    @Test
    void 소문자_미포함() {
        assertThatThrownBy(() -> new UserPassword("A!1BCDEFG")).isInstanceOf(UserException.class);
    }

    @Test
    void 특수문자_미포함() {
        assertThatThrownBy(() -> new UserPassword("a!1bcdefg")).isInstanceOf(UserException.class);
    }

    @Test
    void 숫자_미포함() {
        assertThatThrownBy(() -> new UserPassword("A!bcdefg")).isInstanceOf(UserException.class);
    }

    @Test
    void 여덟자_미만() {
        assertThatThrownBy(() -> new UserPassword("A1!bcde")).isInstanceOf(UserException.class);
    }

    @Test
    void 정상_케이스() {
        assertDoesNotThrow(() -> new UserPassword("A!1bcdefg"));
    }
}