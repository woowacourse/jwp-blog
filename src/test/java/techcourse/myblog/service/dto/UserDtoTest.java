package techcourse.myblog.service.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import techcourse.myblog.support.ValidatorTests;
import techcourse.myblog.support.validation.UserGroups.*;


public class UserDtoTest extends ValidatorTests {
    @Test
    void 올바른_이름_테스트() {
        UserDto userDto = new UserDto("abc", "abcd@naver.com", "aA!123456789");
        checkValidate(userDto, true);
    }

    @Test
    void 올바르지_않은_문자가들어간_이름_테스트() {
        UserDto userDto = new UserDto("aaa!!", "abcd@naver.com", "aA!123456789");
        checkValidate(userDto, false, Edit.class);
    }

    @Test
    void 두_글자_미만의_이름_테스트() {
        UserDto userDto = new UserDto("a", "abcd@naver.com", "aA!123456789");
        checkValidate(userDto, false, Edit.class);
    }

    @Test
    void 열_글자_초과의_이름_테스트() {
        UserDto userDto = new UserDto("abcdfabcdfe", "abcd@naver.com", "aA!123456789");
        checkValidate(userDto, false, Edit.class);
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
        checkValidate(userDto, false);
    }

    @Test
    void 올바르지_않은_이메일_테스트() {
        UserDto userDto = new UserDto("abcd", "abcdnaver.com", "aA!123456789");
        checkValidate(userDto, false);
    }
}
