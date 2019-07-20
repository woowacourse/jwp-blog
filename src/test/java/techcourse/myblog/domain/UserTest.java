package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Java6Assertions.assertThat;

public class UserTest {
    @Test
    void equalPassword() {
        String password = "qweqwe";
        assertThat(new User("name", "email@gmail.com", password).equalPassword(password)).isTrue();
    }
}
