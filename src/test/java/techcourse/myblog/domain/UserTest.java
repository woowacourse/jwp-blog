package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void matchPassword() {
        User user = User.builder()
                .name("이름")
                .email("test@test.com")
                .password("pwd")
                .build();

        assertThat(user.matchPassword("pwd")).isTrue();
        assertThat(user.matchPassword("pwd2")).isFalse();
    }
}