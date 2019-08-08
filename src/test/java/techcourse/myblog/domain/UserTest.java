package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserTest {
    @Test
    void 정상생성() {
        assertDoesNotThrow(() -> new User("bmo", "Password123!", "test@gmail.com"));
    }
}
