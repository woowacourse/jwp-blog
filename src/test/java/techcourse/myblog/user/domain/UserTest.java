package techcourse.myblog.user.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.user.exception.SignUpException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserTest {

    @Test
    void 올바른_회원_가입() {
        assertDoesNotThrow(() -> new User("ike", "Aa1!bcdef", "ike@gmail.com"));
    }

    @Test
    void 이름에_특수문자가_있을경우() {
        assertThatThrownBy(() -> new User("aa!", "Aa1!bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 이름이_2자_미만일_경우() {
        assertThatThrownBy(() -> new User("a", "Aa1!bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 이름이_8자_초과일_경우() {
        assertThatThrownBy(() -> new User("aaaaaaaaaaa", "Aa1!bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 이름에_숫자가_있을_경우() {
        assertThatThrownBy(() -> new User("ike1", "Aa1!bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 패스워드_대문자가_없을경우() {
        assertThatThrownBy(() -> new User("ike", "aa1!bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 패스워드_소문자가_없을경우() {
        assertThatThrownBy(() -> new User("ike", "AA1!BCDEF", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 패스워드_특수문자가_없을경우() {
        assertThatThrownBy(() -> new User("ike", "Aa12bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }

    @Test
    void 패스워드_숫자가_없을경우() {
        assertThatThrownBy(() -> new User("ike", "Aa@!bcdef", "ike@gmail.com"))
                .isInstanceOf(SignUpException.class);
    }
}