package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    @Test
    void 정상생성() {
        assertThatCode(() -> new User("BOB", "test@gmail.com", "PAssWord!2"))
                .doesNotThrowAnyException();
    }

    @Test
    void 이름_10자초과_오류() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("BOBBBBBBBBB", "test@gmail.com", "PAssword!2"));
    }

    @Test
    void 이름_숫자_오류() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("123", "test@gmail.com", "PAssword!2"));
    }

    @Test
    void 이름_특수문자_오류() {
        assertThrows(IllegalArgumentException.class,
                () -> new User("@#$", "test@gmail.com", "PAssword!2"));
    }

    @Test
    void isCorrectPassword() {
    }

    @Test
    void isWrongPassword() {
    }
}
