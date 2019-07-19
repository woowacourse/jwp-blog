package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserTest {

    @Test
    void NAME_VALIDATE() {
        assertThatThrownBy(() -> new User("aa!", "abcd", "a"))
                .isInstanceOf(UserException.class);
    }

    @Test
    void NAME_VALIDATE2() {
        assertThatThrownBy(() -> new User("a", "abcd", "a"))
                .isInstanceOf(UserException.class);
    }

    @Test
    void NAME_VALIDATE3() {
        assertThatThrownBy(() -> new User("aaaaaaaaaaa", "abcd", "a"))
                .isInstanceOf(UserException.class);
    }

    @Test
    void NAME_VALIDATE4() {
        assertThatThrownBy(() -> new User("aa1", "abcd", "a"))
                .isInstanceOf(UserException.class);
    }

    @Test
    void PASSWORD_VALIDATE() {
        assertThatThrownBy(() -> new User("aaa", "abcdefgh", "a"))
                .isInstanceOf(UserException.class);
    }

    @Test
    void PASSWORD_VALIDATE2() {
        assertDoesNotThrow(() -> new User("aaa", "Aa1!bcdef", "a"));
    }

    @Test
    void PASSWORD_VALIDATE3() {
        assertThatThrownBy(() -> new User("aaa", "Abcdefgh", "a"))
                .isInstanceOf(UserException.class);
    }

    @Test
    void PASSWORD_VALIDATE4() {
        assertThatThrownBy(() -> new User("aaa", "Ab!cdefgh", "a"))
                .isInstanceOf(UserException.class);
    }
}