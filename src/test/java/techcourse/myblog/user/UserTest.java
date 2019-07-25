package techcourse.myblog.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    private User user;

    @BeforeEach
    void setUp() {
        user = new User("buddy", "buddy@gmail.com", "Aa12345!");
    }

    @Test
    void 로그인_시_패스워드_일치() {
        assertThat(user.matchPassword("Aa12345!")).isTrue();
    }

    @Test
    void 로그인_시_패스워드_불일치() {
        assertThat(user.matchPassword("sdasdasda")).isFalse();
    }

    @Test
    void 유저이름_변경() {
        user.changeUserName("ssosso");
        assertThat(user.getUserName()).isEqualTo("ssosso");
    }
}
