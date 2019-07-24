package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.exception.InvalidUserDataException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    private static final long USER_ID = 0l;
    private static final String VALID_NAME = "코니";
    private static final String VALID_EMAIL = "cony@cony.com";
    private static final String VALID_PASSWORD = "@Password12";

    @Test
    public void 모든_항목이_조건에_맞는_경우_객체를_생성한다() {
        assertDoesNotThrow(() -> new User(USER_ID, VALID_NAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void 이름에_숫자가_들어가면_예외를_던진다() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, "123코니", VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void 이름에_특수문자가_들어가면_예외를_던진다() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, "코니!", VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void 이메일의_형식이_틀리면_예외를_던진다() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, VALID_NAME, "cony@", VALID_PASSWORD));
    }

    @Test
    public void 비밀번호가_8자_미만이면_예외를_던진다() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, VALID_NAME, VALID_EMAIL, "@Passw1"));
    }

    @Test
    public void 비밀번호에_숫자가_없으면_예외를_던진다() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, VALID_NAME, VALID_EMAIL, "@Passwordhaha"));
    }

    @Test
    public void 비밀번호에_특수문자가_없으면_예외를_던진다() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, VALID_NAME, VALID_EMAIL, "Password12"));
    }
}