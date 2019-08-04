package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.application.dto.UserRequestDto;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserTest {
    private static final Long ID = 1L;
    private static final String EMAIL = "aa@naver.com";
    private static final String NAME = "zino";
    private static final String PASSWORD = "password";

    Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();

    @Test
    void 올바른_User_생성_테스트() {
        assertDoesNotThrow(() -> new User(EMAIL, NAME, PASSWORD));
    }

    @Test
    void 올바르지_않은_email_User_생성() {
        assertThat(validator.validate(new User("aa", NAME, PASSWORD)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_name_User_생성() {
        assertThat(validator.validate(new User(EMAIL, "z", PASSWORD)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_password_User_생성() {
        assertThat(validator.validate(new User(EMAIL, NAME, "pass")).isEmpty()).isFalse();
    }

    @Test
    void 수정_테스트() {
        User user = new User(ID, EMAIL, NAME, PASSWORD);
        UserRequestDto requestDto = new UserRequestDto(EMAIL, NAME + "a", PASSWORD + "a");
        user.modify(requestDto);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getName()).isEqualTo(NAME + "a");
        assertThat(user.getPassword()).isEqualTo(PASSWORD + "a");
    }
}
