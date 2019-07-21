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
    private static final String INVALID_NAME = "123";
    private static final String INVALID_EMAIL = "123";
    private static final String INVALID_PASSWORD = "123";

    @Test
    public void 모든_항목이_조건에_맞는_경우_객체_생성() {
        assertDoesNotThrow(() -> new User(USER_ID, VALID_NAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void 조건에_맞지_않는_이름을_등록했을_경우_예외처리() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, INVALID_NAME, VALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void 조건에_맞지_않는_이메일을_등록했을_경우_예외처리() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, VALID_NAME, INVALID_EMAIL, VALID_PASSWORD));
    }

    @Test
    public void 조건에_맞지_않는_비밀번호를_등록했을_경우_예외처리() {
        assertThrows(InvalidUserDataException.class,
                () -> new User(USER_ID, VALID_NAME, VALID_EMAIL, INVALID_PASSWORD));
    }
}