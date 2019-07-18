package techcourse.myblog.dto;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserDtoTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void 올바른_이름_테스트() {
        UserDto userDto = new UserDto("abc", "abcd@naver.com", "aA!123456789");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void 올바르지_않은_문자가들어간_이름_테스트() {
        UserDto userDto = new UserDto("aaa!!", "abcd@naver.com", "aA!123456789");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void 두_글자_미만의_이름_테스트() {
        UserDto userDto = new UserDto("a", "abcd@naver.com", "aA!123456789");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void 열_글자_초과의_이름_테스트() {
        UserDto userDto = new UserDto("abcdfabcdfe", "abcd@naver.com", "aA!123456789");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    /**
     * 1. 소문자, 대문자, 특수문자가 포함되지 않은 경우
     * 2. 8자리 미만인 경우
     * 3. 소문자가 포함되지 않은 경우
     * 4. 대문자가 포함되지 않은 경우
     * 5. 특수문자가 포함되지 않은 경우
     */
    @ParameterizedTest
    @CsvSource(value = {"12345678", "1234aA!", "12345678A!", "12345678a!", "12345678aA"})
    void 올바르지_않은_비밀번호_테스트(String password) {
        UserDto userDto = new UserDto("abcd", "abcd@naver.com", password);
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void 올바르지_않은_이메일_테스트() {
        UserDto userDto = new UserDto("abcd", "abcdnaver.com", "aA!123456789");
        Set<ConstraintViolation<UserDto>> violations = validator.validate(userDto);
        assertFalse(violations.isEmpty());
    }
}
