package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class UserTest {
    @Test
    void 정상생성() {
        assertDoesNotThrow(() -> new User("bmo", "Password123!", "test@gmail.com"));
    }
}
