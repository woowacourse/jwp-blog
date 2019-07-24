package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.service.dto.UserDto;
import techcourse.myblog.support.ValidatorTests;

import java.util.stream.Stream;

public class UserTests extends ValidatorTests {
    @ParameterizedTest(name = "{index} : {3}")
    @MethodSource("invalidUser")
    /**
     * 1. 이름이 잘못된 경우
     * 2. 이메일이 잘못된 경우
     * 3. 비밀번호가 잘몰된 경우
     */
    void 올바르지_않은_유저(String name, String email, String password) {
        UserDto userDto = new UserDto(name, email, password);
        checkValidate(userDto.toUser(), false);
    }

    private static Stream<Arguments> invalidUser() {
        return Stream.of(
                Arguments.of(null, "", "eara12sa@naver.copm", "abcd1234!!@@"),
                Arguments.of(null, "ab", "eara12sa", "abcd1234!!@@"),
                Arguments.of(null, "ab", "eara12sa@naver.com", "abcd1234")
        );
    }

    @Test
    void 올바른_유저() {
        UserDto userDto = new UserDto("ab", "eara12sa@naver.com", "1234!Aa12");
        checkValidate(userDto.toUser(), true);
    }
}
