package techcourse.myblog.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.validation.UserInfo;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.groups.Default;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static techcourse.myblog.validation.UserPattern.*;

class UserDtoTests {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    static Stream<Arguments> invalidUserParameters() {
        return Stream.of(
                Arguments.of("11!!", "e@mail.com", "p@sswsavedPassw0RD!", NAME_CONSTRAINT_MESSAGE),
                Arguments.of("name", "saved@", "edPassw0RD!", EMAIL_CONSTRAINT_MESSAGE),
                Arguments.of("name", "saved@mail.com", "password", PASSWORD_CONSTRAINT_MESSAGE),
                Arguments.of(null, "e@mail.com", "p@sswsavedPassw0RD!", EMPTY_CONSTRAINT_MESSAGE)
        );
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidUserParameters")
    void User_생성_검증_테스트(String name, String email, String password, String msg) {
        Set<ConstraintViolation<UserDto>> constraintViolations = validator.validate(new UserDto(name, email, password), UserInfo.class, Default.class);
        assertThat(constraintViolations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly(msg);
    }

}