package techcourse.myblog.web.dto;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class UserRequestDtoTest {

    @Autowired
    private Validator validator;

    @Test
    void create() {
        assertDoesNotThrow(() -> UserRequestDto.of("jame", "aaa@mail.com", "p@ssW0rd", "p@ssW0rd"));
    }

    @Test
    void invalid_email() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("james", "aaa", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.EMAIL_FORMAT_MESSAGE);
    }

    private void assertUserRequestFirstViolationMessage(UserRequestDto userRequestDto, String expectedMessage) {
        Set<ConstraintViolation<UserRequestDto>> violations = validator.validate(userRequestDto);
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(expectedMessage);
    }

    @Test
    void email_without_user_id() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("james", "@email.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.EMAIL_FORMAT_MESSAGE);
    }

    @Test
    void email_without_at() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("james", "email.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.EMAIL_FORMAT_MESSAGE);
    }

    @Test
    void username_non_alphabet() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("김철수", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.USERNAME_FORMAT_MESSAGE);
    }

    @Test
    void username_under_size() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("a", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.USERNAME_LENGTH_MESSAGE);
    }

    @Test
    void username_over_size() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("abcdefghijkl", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.USERNAME_LENGTH_MESSAGE
        );
    }

    @Test
    void username_include_number() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("john123", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.USERNAME_FORMAT_MESSAGE
        );
    }

    @Test
    void username_include_special_char() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("john!", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.USERNAME_FORMAT_MESSAGE
        );
    }

    @Test
    void password_under_size() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "p@ssW0", "p@ssW0"),
            UserRequestDto.PASSWORD_LENGTH_MESSAGE
        );
    }

    @Test
    void password_not_equal_to_confirm() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "p@ssW0rd", "p@ssW0rdd"),
            UserRequestDto.PASSWORD_EQUALITY_MESSAGE
        );
    }

    @Test
    void password_without_uppercase() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "p@ssw0rddddd", "p@ssw0rddddd"),
            UserRequestDto.PASSWORD_FORMAT_MESSAGE
        );
    }

    @Test
    void password_without_lowercase() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "P@SSW0RD", "P@SSW0RD"),
            UserRequestDto.PASSWORD_FORMAT_MESSAGE
        );
    }
}
