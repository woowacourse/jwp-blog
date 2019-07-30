package techcourse.myblog.domain;

import org.junit.jupiter.api.Test;
import techcourse.myblog.domain.user.Email;
import techcourse.myblog.domain.user.UserException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class EmailTest {

    @Test
    void 이름_테스트() {
        assertThatThrownBy(() -> new Email("가나다@gmail.com")).isInstanceOf(UserException.class);
    }

    @Test
    void 골뱅이_테스트() {
        assertThatThrownBy(() -> new Email("abc#gmail.com")).isInstanceOf(UserException.class);
    }

    @Test
    void 도메인_테스트() {
        assertThatThrownBy(() -> new Email("abc@!gmail.com")).isInstanceOf(UserException.class);
    }

    @Test
    void 정상_테스트() {
        assertDoesNotThrow(() -> new Email("abc@gmail.com"));
    }
}