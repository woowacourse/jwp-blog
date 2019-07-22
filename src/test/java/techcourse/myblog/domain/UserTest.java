package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {

    @Test
    void authentication() {
        User newUser = User.of("james", "authentication_test@email.com", "p@ssW0rd");
        assertThat(newUser.authenticate("p@ssW0rd")).isTrue();
        assertThat(newUser.authenticate("p@ssW0rd23")).isFalse();
    }
}
