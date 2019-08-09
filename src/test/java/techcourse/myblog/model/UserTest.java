package techcourse.myblog.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {
    private String PASSWORD = "!@QW12qw";

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("name", "email@email.com", PASSWORD);
    }

    @Test
    @DisplayName("사용자 정보를 변경한다.")
    void updateUser() {
        User updatedUser = new User("updated", "email2@email.com", "!@QW12qw2");

        user.update(updatedUser);
        assertThat(user.getUserName()).isEqualTo(updatedUser.getUserName());
        assertThat(user.getEmail()).isEqualTo(updatedUser.getEmail());
        assertThat(user.getPassword()).isEqualTo(updatedUser.getPassword());
    }

    @Test
    @DisplayName("비밀번호가 맞는지 체크한다.")
    void checkPassword() {
        assertThat(user.checkPassword(PASSWORD)).isTrue();
        assertThat(user.checkPassword("wrong" + PASSWORD)).isFalse();
    }
}