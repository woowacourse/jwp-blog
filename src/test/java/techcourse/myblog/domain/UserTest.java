package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

<<<<<<<HEAD
=======
        >>>>>>>upstream/school0bhy
        <<<<<<<HEAD
=======
        >>>>>>>upstream/school0bhy

class UserTest {
    private User user;

    @BeforeEach
    void setup() {
        user = new User("name", "e@mail.com", "pAssw!rd0");
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    void User_생성_테스트(String name, String email, String password, String field) {
        assertThrows(IllegalUserException.class, () ->
                new User(name, email, password)
        );
    }

    static Stream<Arguments> invalidParameters() throws Throwable {
        return Stream.of(
                Arguments.of("wrong!name", "e@mail.com", "p!ssw0rD", "name"),
                Arguments.of("name", "wrong", "p!ssw00Rd", "email"),
                Arguments.of("name", "e@mail.com", "잘못된패스워드", "password")
        );
    }
<<<<<<< HEAD
=======

    @ParameterizedTest(name = "{index}: {1}")
    @MethodSource("userAuthResultStream")
    void authenticate_fail_test(String email, String password) {
        assertThrows(AuthenticationFailedException.class, () ->
                user.authenticate(email, password));
    }

    static Stream<Arguments> userAuthResultStream() throws Throwable {
        return Stream.of(
                Arguments.of("mail@email.com", "pAssw!rd0"),
                Arguments.of("e@mail.com", "Passw0rd!")
        );
    }

    @Test
    void authenticate_success_test() {
        assertDoesNotThrow(() -> user.authenticate("e@mail.com", "pAssw!rd0"));
    }
>>>>>>> upstream/school0bhy
}