package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class UserTest {
    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void 이메일_형식_예외_처리() {
        User user = new User("권민철", "kwon140naver", "12345678");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations.isEmpty()).isFalse();
        violations.forEach(violation ->
                assertThat(violation.getMessage()).isEqualTo("이메일 양식을 지켜주세요.")
        );
    }

    @Test
    public void 비밀번호_길이_예외_처리() {
        User user = new User("권민철", "kwon140@naver.com", "1234567");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations.isEmpty()).isFalse();
        violations.forEach(violation ->
                assertThat(violation.getMessage()).isEqualTo("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
        );
    }

    @Test
    public void 비밀번호_문자_형식_예외() {
        User user = new User("권민철", "kwon140@naver.com", "권민철권민철루피루피");

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertThat(violations.isEmpty()).isFalse();
        violations.forEach(violation ->
                assertThat(violation.getMessage()).isEqualTo("비밀번호는 8자 이상, 소문자, 대문자, 숫자, 특수문자의 조합으로 입력하세요.")
        );
    }

    @Test
    public void 모든_조건_정상_User_객체_생성() {
        assertDoesNotThrow(() -> {
            new User("권민철", "kwon140@naver.com", "12345678");
        });
    }
}
