package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import techcourse.myblog.domain.exception.InvalidEmailFormatException;
import techcourse.myblog.domain.exception.InvalidPasswordFormatException;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    public void 이메일_형식_예외_처리() {
        assertThrows(InvalidEmailFormatException.class, () -> {
            new User("권민철", "kwon140naver", "12345678");
        });
    }

    @Test
    public void 비밀번호_길이_예외_처리() {
        assertThrows(InvalidPasswordFormatException.class, () -> {
            new User("권민철", "kwon140@naver.com", "1234567");
        });
    }

    @Test
    public void 비밀번호_문자_형식_예외() {
        assertThrows(InvalidPasswordFormatException.class, () -> {
            new User("권민철", "kwon140@naver.com", "권민철권민철루피루피");
        });
    }

    @Test
    public void 모든_조건_정상_User_객체_생성() {
        assertDoesNotThrow(() -> {
            new User("권민철", "kwon140@naver.com", "12345678");
        });
    }
}
