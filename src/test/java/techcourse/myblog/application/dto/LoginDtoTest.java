package techcourse.myblog.application.dto;

import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class LoginDtoTest {
    Validator validator = Validation.byDefaultProvider().configure().buildValidatorFactory().getValidator();

    @Test
    void 올바른_LoginDTO_생성_테스트() {
        String email = "aa@naver.com";
        String password = "password";
        assertDoesNotThrow(() -> new LoginDto(email, password));
    }

    @Test
    void 올바르지_않은_email_LoginDTO_생성_에러() {
        String email = "aa";
        String password = "password";

        assertThat(validator.validate(new LoginDto(email, password)).isEmpty()).isFalse();
    }

    @Test
    void 올바르지_않은_password_LoginDTO_생성_에러() {
        String email = "aa@naver.com";
        String password = "pass";

        assertThat(validator.validate(new LoginDto(email, password)).isEmpty()).isFalse();
    }
}
