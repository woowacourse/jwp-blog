package techcourse.myblog.users;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidArguments")
    @DisplayName("User 생성 유효성 검사")
    void validateUser(String email, String password, String name, String message) {
        assertThrows(IllegalArgumentException.class, () ->
                User.builder()
                        .email(email)
                        .password(password)
                        .name(name)
                        .build());
    }

    static Stream<Arguments> invalidArguments() {
        final String validEmail = "email@gmail.com";
        final String validPassword = "password1234!";
        final String validName = "name";

        return Stream.of(
                Arguments.of("email", validPassword, validName, "이메일 형식 미준수"),
                Arguments.of(validEmail, validPassword, "n", "이름 한글자"),
                Arguments.of(validEmail, validPassword, "nf23", "이름 숫자 포함"),
                Arguments.of(validEmail, validPassword, "qwertasdfgzx", "이름 10자 초과"),
                Arguments.of(validEmail, "Passw0rd", validName, "password 특수문자 제외"),
                Arguments.of(validEmail, "P@ssword", validName, "password 숫자 제외"),
                Arguments.of(validEmail, "p@ssw0rd", validName, "password 대문자 제외"),
                Arguments.of(validEmail, "P@SSW0RD", validName, "password 소문자 제외")
        );
    }

}