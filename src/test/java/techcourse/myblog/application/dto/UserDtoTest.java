package techcourse.myblog.application.dto;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserDtoTest {
    Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();

    @Test
    void 올바른_UserDTO_생성_테스트() {
        String email = "aa@naver.com";
        String name = "zino";
        String password = "password";

        assertDoesNotThrow(() -> new UserDto(email, name, password));
    }

    @Test
    void 올바르지_않은_email_UserDTO_생성에러() {
        String email = "aa";
        String name = "zino";
        String password = "password";

        assertThat(validator.validate(new UserDto(email, name, password)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_name_UserDTO_생성에러() {
        String email = "aa@naver.com";
        String name = "z";
        String password = "password";

        assertThat(validator.validate(new UserDto(email, name, password)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_password_UserDTO_생성에러() {
        String email = "aa@naver.com";
        String name = "zino";
        String password = "pass";

        assertThat(validator.validate(new UserDto(email, name, password)).isEmpty()).isFalse();
    }
}
