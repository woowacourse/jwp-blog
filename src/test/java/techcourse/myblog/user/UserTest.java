package techcourse.myblog.user;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    public static final User user = User.builder()
            .userName("buddy")
            .email("buddy@gmail.com")
            .password("Aa12345!")
            .build();

    public static final User user2 = User.builder()
            .userName("buddy2")
            .email("buddy2@gmail.com")
            .password("Aa12345!")
            .build();

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
        User user2 = new User("buddy", "buddy@gmail.com", "Aa12345!");

        user2.changeUserName("ssosso");

        assertThat(user2.getUserName()).isEqualTo("ssosso");
    }
}
