package techcourse.myblog.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import techcourse.myblog.domain.exception.InvalidUserException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    static Stream<Arguments> invalidParameters() {
        return Stream.of(
                Arguments.of("wrong!name", "e@mail.com", "p!ssw0rD", "wrong name"),
                Arguments.of("name", "wrong", "p!ssw00Rd", "wrong email"),
                Arguments.of("name", "e@mail.com", "잘못된패스워드", "wrong password")
        );
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    void User_생성_테스트(String name, String email, String password, String field) {
        assertThrows(InvalidUserException.class, () ->
                new User(name, email, password)
        );
    }
}