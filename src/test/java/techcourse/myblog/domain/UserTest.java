package techcourse.myblog.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserTest {
    User user;
    final String password = "P@ssw0rd";

    @BeforeEach
    void setUp() {
        user = User.builder()
                .name("name")
                .email("email@gmail.com")
                .password(password)
                .build();
    }

    @ParameterizedTest(name = "{index}: {3}")
    @MethodSource("invalidParameters")
    @DisplayName("User 생성 유효성 검사")
    void invalidCreate(String name, String email, String password, String message) {
        assertThrows(IllegalArgumentException.class, () -> User.builder()
                .name("name")
                .password("P@ssWwrd")
                .email("asd@ads")
                .build());
    }

    static Stream<Arguments> invalidParameters() throws Throwable {
        return Stream.of(
                Arguments.of("a", "asdf@asd", "P@assw0rd", "name 2자 미만"),
                Arguments.of("qwertasdfzp", "asdf@asd", "P@assw0rd", "name 10자 초과"),
                Arguments.of("12ad", "asdf@asd", "P@assw0rd", "name 숫자 포함"),
                Arguments.of("name", "asdf@asd", "Passw0rd", "password 특수문자 제외"),
                Arguments.of("name", "asdf@asd", "P@ssword", "password 숫자 제외"),
                Arguments.of("name", "asdf@asd", "p@ssw0rd", "password 대문자 제외"),
                Arguments.of("name", "asdf@asd", "P@SSW0RD", "password 소문자 제외"),
                Arguments.of("name", "asdfasd", "P@SSW0RD", "email 양식")
        );
    }

    @Test
    void authenticate_패스워드_일치() {
        assertThat(user.authenticate(password)).isTrue();
    }

    @Test
    void authenticate_패스워드_불일치() {
        assertThat(user.authenticate(password + "good")).isFalse();
    }
}