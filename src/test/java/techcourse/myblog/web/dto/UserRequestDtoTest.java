package techcourse.myblog.web.dto;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
public class UserRequestDtoTest {

    private static ValidatorFactory validatorFactory;

    private Validator validator;

    @BeforeAll
    static void init() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
    }

    @AfterAll
    static void cleanup() {
        validatorFactory.close();
    }

    @BeforeEach
    void before() {
        validator = validatorFactory.getValidator();
    }

    @Test
    void create() {
        assertDoesNotThrow(() -> UserRequestDto.of("jame", "aaa@mail.com", "p@ssW0rd", "p@ssW0rd"));
    }

    @Test
    void invalid_email() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("james", "aaa", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_EMAIL_FORMAT);
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
            UserRequestDto.CONSTRAINT_VIOLATION_EMAIL_FORMAT);
    }

    @Test
    void email_without_at() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("james", "email.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_EMAIL_FORMAT);
    }

    @Test
    void username_non_alphabet() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("김철수", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_USERNAME_FORMAT);
    }

    @Test
    void username_under_size() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("a", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_USERNAME_SIZE);
    }

    @Test
    void username_over_size() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("abcdefghijkl", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_USERNAME_SIZE
        );
    }

    @Test
    void username_include_number() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("john123", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_USERNAME_FORMAT
        );
    }

    @Test
    void username_include_special_char() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("john!", "test1@example.com", "p@ssW0rd", "p@ssW0rd"),
            UserRequestDto.CONSTRAINT_VIOLATION_USERNAME_FORMAT
        );
    }

    @Test
    void password_under_size() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "p@ssW0", "p@ssW0"),
            UserRequestDto.CONSTRAINT_VIOLATION_PASSWORD_SIZE
        );
    }

    @Test
    void password_not_equal_to_confirm() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "p@ssW0rd", "p@ssW0rdd"),
            UserRequestDto.CONSTRAINT_VIOLATION_PASSWORD_EQUALITY
        );
    }

    @Test
    void password_without_uppercase() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "p@ssw0rddddd", "p@ssw0rddddd"),
            UserRequestDto.CONSTRAINT_VIOLATION_PASSWORD_FORMAT
        );
    }

    @Test
    void password_without_lowercase() {
        assertUserRequestFirstViolationMessage(
            UserRequestDto.of("haaaaa", "test2@example.com", "P@SSW0RD", "P@SSW0RD"),
            UserRequestDto.CONSTRAINT_VIOLATION_PASSWORD_FORMAT
        );
    }
}
