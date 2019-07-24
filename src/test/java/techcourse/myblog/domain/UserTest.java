package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserTest {
    @Test
    void equalPassword() {
        String email = "email@gmail.com";
        String password = "qweqwe";
        assertThat(new User("name", email, password).authenticate(email, password)).isTrue();
    }
}
